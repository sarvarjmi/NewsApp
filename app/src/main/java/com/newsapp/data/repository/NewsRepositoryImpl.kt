package com.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.newsapp.data.local.dao.BookmarkDao
import com.newsapp.data.local.mapper.toDomain
import com.newsapp.data.local.mapper.toEntity
import com.newsapp.data.remote.api.ApiConstants
import com.newsapp.data.remote.api.NewsApiService
import com.newsapp.data.remote.paging.NewsPagingSource
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [NewsRepository] using Paging 3 for remote data 
 * and Room for local bookmarking.
 */
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val bookmarkDao: BookmarkDao
) : NewsRepository {

    /**
     * Paging Configuration:
     * - [pageSize]: 20. Optimized for the News API limit and mobile data usage.
     * - [prefetchDistance]: 5. Starts loading the next page when the user is 5 items from the end.
     * - [enablePlaceholders]: false. Prevents showing "empty" cards when the remote count is uncertain.
     * - [initialLoadSize]: 20. Matches page size to prevent over-fetching on start.
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
                    newsApiService = newsApiService,
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
                    newsApiService = newsApiService,
                    query = query
                )
            }
        ).flow
    }

    override suspend fun upsertArticle(article: Article) {
        bookmarkDao.upsert(article.toEntity())
    }

    override suspend fun deleteArticle(article: Article) {
        bookmarkDao.delete(article.toEntity())
    }

    override fun getBookmarkedArticles(): Flow<List<Article>> {
        return bookmarkDao.getBookmarks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getArticleByUrl(url: String): Article? {
        return bookmarkDao.getBookmarkByUrl(url)?.toDomain()
    }
}
