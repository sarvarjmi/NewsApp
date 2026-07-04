package com.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.newsapp.data.local.datasource.LocalNewsDataSource
import com.newsapp.data.local.mapper.toDomain
import com.newsapp.data.local.mapper.toEntity
import com.newsapp.data.remote.api.ApiConstants
import com.newsapp.data.remote.datasource.RemoteNewsDataSource
import com.newsapp.data.remote.paging.NewsPagingSource
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [NewsRepository] coordinating between remote 
 * and local data sources.
 *
 * This implementation leverages Paging 3 for the remote API data stream 
 * and a Local Data Source for bookmark persistence.
 */
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteNewsDataSource,
    private val localDataSource: LocalNewsDataSource
) : NewsRepository {

    /**
     * Paging Configuration:
     * - [pageSize]: 20. Aligned with News API default limits.
     * - [prefetchDistance]: 5. Smooth scrolling by fetching next page in advance.
     * - [enablePlaceholders]: false. Simplifies UI logic by only showing loaded items.
     */
    private val pagingConfig = PagingConfig(
        pageSize = ApiConstants.DEFAULT_PAGE_SIZE,
        prefetchDistance = 5,
        enablePlaceholders = false,
        initialLoadSize = ApiConstants.DEFAULT_PAGE_SIZE
    )

    override fun getTopHeadlines(category: String?): Flow<PagingData<Article>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                NewsPagingSource(
                    remoteDataSource = remoteDataSource,
                    category = category
                )
            }
        ).flow
    }

    override fun searchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                NewsPagingSource(
                    remoteDataSource = remoteDataSource,
                    query = query
                )
            }
        ).flow
    }

    override suspend fun refreshNews() {
        // Paging 3 automatically handles refresh via the UI layer's 'adapter.refresh()'.
    }

    override suspend fun saveBookmark(article: Article) {
        localDataSource.upsertBookmark(article.toEntity())
    }

    override suspend fun removeBookmark(article: Article) {
        localDataSource.deleteBookmark(article.toEntity())
    }

    override suspend fun toggleBookmark(article: Article) {
        val exists = localDataSource.isBookmarked(article.url)
        if (exists) {
            localDataSource.deleteBookmark(article.toEntity())
        } else {
            localDataSource.upsertBookmark(article.toEntity())
        }
    }

    override fun observeBookmarks(): Flow<List<Article>> {
        return localDataSource.getBookmarks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return localDataSource.isBookmarked(url)
    }
}
