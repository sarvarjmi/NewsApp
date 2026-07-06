package com.newsapp.data.local.datasource

import androidx.paging.PagingSource
import com.newsapp.data.local.dao.BookmarkDao
import com.newsapp.data.local.dao.NewsArticleDao
import com.newsapp.data.local.entity.BookmarkEntity
import com.newsapp.data.local.entity.NewsArticleEntity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for the Local Data Source.
 */
interface LocalNewsDataSource {
    // Bookmark operations
    suspend fun upsertBookmark(bookmark: BookmarkEntity)
    suspend fun deleteBookmark(bookmark: BookmarkEntity)
    fun getBookmarks(): Flow<List<BookmarkEntity>>
    suspend fun getBookmarkByUrl(url: String): BookmarkEntity?
    suspend fun isBookmarked(url: String): Boolean

    // Cache operations for the news feed
    suspend fun upsertArticles(articles: List<NewsArticleEntity>)
    fun getArticlesByCategory(category: String): Flow<List<NewsArticleEntity>>
    fun getArticlesPagingSource(category: String): PagingSource<Int, NewsArticleEntity>
    suspend fun deleteOldArticles(threshold: Long)
    suspend fun clearAllArticles()
    suspend fun deleteArticlesByCategory(category: String)
    suspend fun getCachedArticleByUrl(url: String): NewsArticleEntity?
}

/**
 * Implementation of [LocalNewsDataSource] using Room DAOs.
 */
@Singleton
class LocalNewsDataSourceImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val newsArticleDao: NewsArticleDao
) : LocalNewsDataSource {

    override suspend fun upsertBookmark(bookmark: BookmarkEntity) {
        bookmarkDao.upsert(bookmark)
    }

    override suspend fun deleteBookmark(bookmark: BookmarkEntity) {
        bookmarkDao.delete(bookmark)
    }

    override fun getBookmarks(): Flow<List<BookmarkEntity>> {
        return bookmarkDao.getBookmarks()
    }

    override suspend fun getBookmarkByUrl(url: String): BookmarkEntity? {
        return bookmarkDao.getBookmarkByUrl(url)
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return bookmarkDao.isBookmarked(url)
    }

    override suspend fun upsertArticles(articles: List<NewsArticleEntity>) {
        newsArticleDao.upsertArticles(articles)
    }

    override fun getArticlesByCategory(category: String): Flow<List<NewsArticleEntity>> {
        return newsArticleDao.getArticlesByCategory(category)
    }

    override fun getArticlesPagingSource(category: String): PagingSource<Int, NewsArticleEntity> {
        return newsArticleDao.getArticlesPagingSource(category)
    }

    override suspend fun deleteOldArticles(threshold: Long) {
        newsArticleDao.deleteOldArticles(threshold)
    }

    override suspend fun clearAllArticles() {
        newsArticleDao.clearAll()
    }

    override suspend fun deleteArticlesByCategory(category: String) {
        newsArticleDao.deleteArticlesByCategory(category)
    }

    override suspend fun getCachedArticleByUrl(url: String): NewsArticleEntity? {
        return try {
            newsArticleDao.getArticleByUrl(url)
        } catch (e: Exception) {
            null
        }
    }
}
