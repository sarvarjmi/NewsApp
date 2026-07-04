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
 * Concrete implementation of [NewsRepository] coordinating between remote 
 * and local data sources.
 *
 * This implementation leverages Paging 3 for the remote API data stream 
 * and Room for bookmark persistence.
 */
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val bookmarkDao: BookmarkDao
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

    override suspend fun refreshNews() {
        // Paging 3 automatically handles refresh via the UI layer's 'adapter.refresh()'.
        // This method can be expanded to include cache invalidation if needed.
    }

    override suspend fun saveBookmark(article: Article) {
        bookmarkDao.upsert(article.toEntity())
    }

    override suspend fun removeBookmark(article: Article) {
        bookmarkDao.delete(article.toEntity())
    }

    override suspend fun toggleBookmark(article: Article) {
        val exists = bookmarkDao.isBookmarked(article.url)
        if (exists) {
            bookmarkDao.delete(article.toEntity())
        } else {
            bookmarkDao.upsert(article.toEntity())
        }
    }

    override fun observeBookmarks(): Flow<List<Article>> {
        return bookmarkDao.getBookmarks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return bookmarkDao.isBookmarked(url)
    }
}
