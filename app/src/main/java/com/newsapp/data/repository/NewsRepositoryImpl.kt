package com.newsapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.newsapp.core.network.NetworkResult
import com.newsapp.data.local.database.NewsDatabase
import com.newsapp.data.local.datasource.LocalNewsDataSource
import com.newsapp.data.local.entity.NewsRemoteKeysEntity
import com.newsapp.data.local.mapper.toCacheEntity
import com.newsapp.data.local.mapper.toDomain
import com.newsapp.data.local.mapper.toEntity
import com.newsapp.data.remote.api.ApiConstants
import com.newsapp.data.remote.datasource.RemoteNewsDataSource
import com.newsapp.data.remote.mapper.toDomain as toDomainFromRemote
import com.newsapp.data.remote.paging.NewsPagingSource
import com.newsapp.data.remote.paging.NewsRemoteMediator
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Collections
import java.util.WeakHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [NewsRepository] coordinating between remote 
 * and local data sources.
 */
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteNewsDataSource,
    private val localDataSource: LocalNewsDataSource,
    private val database: NewsDatabase
) : NewsRepository {

    private val pagingConfig = PagingConfig(
        pageSize = ApiConstants.DEFAULT_PAGE_SIZE,
        prefetchDistance = 5,
        enablePlaceholders = false,
        initialLoadSize = ApiConstants.DEFAULT_PAGE_SIZE
    )

    private val activePagingSources = Collections.synchronizedSet(
        Collections.newSetFromMap(WeakHashMap<NewsPagingSource, Boolean>())
    )

    private fun invalidateActiveSources() {
        synchronized(activePagingSources) {
            activePagingSources.forEach { it.invalidate() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getTopHeadlines(category: String?): Flow<PagingData<Article>> {
        val targetCategory = category ?: "general"
        return Pager(
            config = pagingConfig,
            remoteMediator = NewsRemoteMediator(
                remoteDataSource = remoteDataSource,
                database = database,
                category = targetCategory
            ),
            pagingSourceFactory = {
                localDataSource.getArticlesPagingSource(targetCategory)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun searchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                NewsPagingSource(
                    remoteDataSource = remoteDataSource,
                    isBookmarked = { localDataSource.isBookmarked(it) },
                    query = query
                ).also { activePagingSources.add(it) }
            }
        ).flow
    }

    override suspend fun refreshNews() {
        val result = remoteDataSource.getTopHeadlines(
            category = "general",
            page = 1,
            pageSize = ApiConstants.DEFAULT_PAGE_SIZE
        )

        when (result) {
            is NetworkResult.Success -> {
                val articles = result.data.articles.map { dto ->
                    val isBookmarked = localDataSource.isBookmarked(dto.url)
                    dto.toDomainFromRemote().copy(isBookmarked = isBookmarked)
                }
                
                database.withTransaction {
                    // Sync with RemoteMediator logic: Clear existing general news and keys
                    database.newsRemoteKeysDao.clearRemoteKeys()
                    database.newsArticleDao.deleteArticlesByCategory("general")
                    
                    val entities = articles.map { it.toCacheEntity("general") }
                    val keys = articles.map { 
                        NewsRemoteKeysEntity(
                            articleUrl = it.url, 
                            prevPage = null, 
                            nextPage = 2 
                        ) 
                    }
                    
                    database.newsArticleDao.upsertArticles(entities)
                    database.newsRemoteKeysDao.upsertRemoteKeys(keys)
                }
                invalidateActiveSources()
            }
            is NetworkResult.Error -> {
                throw result.exception ?: Exception(result.message)
            }
            else -> { /* Ignore loading state */ }
        }
    }

    override suspend fun saveBookmark(article: Article) {
        localDataSource.upsertBookmark(article.toEntity())
        invalidateActiveSources()
    }

    override suspend fun removeBookmark(article: Article) {
        localDataSource.deleteBookmark(article.toEntity())
        invalidateActiveSources()
    }

    override suspend fun toggleBookmark(article: Article) {
        val exists = localDataSource.isBookmarked(article.url)
        if (exists) {
            localDataSource.deleteBookmark(article.toEntity())
        } else {
            localDataSource.upsertBookmark(article.toEntity())
        }
        invalidateActiveSources()
    }

    override fun observeBookmarks(): Flow<List<Article>> {
        return localDataSource.getBookmarks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return localDataSource.isBookmarked(url)
    }

    override suspend fun getArticleFromCache(url: String): Article? {
        // Check bookmarks first
        val bookmarked = localDataSource.getBookmarkByUrl(url)
        if (bookmarked != null) return bookmarked.toDomain()
        
        // Then check temporary cache
        return localDataSource.getCachedArticleByUrl(url)?.toDomain()
    }
}
