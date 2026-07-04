package com.newsapp.data.local.datasource

import com.newsapp.data.local.dao.BookmarkDao
import com.newsapp.data.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for the Local Data Source.
 */
interface LocalNewsDataSource {
    suspend fun upsertBookmark(bookmark: BookmarkEntity)
    suspend fun deleteBookmark(bookmark: BookmarkEntity)
    fun getBookmarks(): Flow<List<BookmarkEntity>>
    suspend fun getBookmarkByUrl(url: String): BookmarkEntity?
    suspend fun isBookmarked(url: String): Boolean
}

/**
 * Implementation of [LocalNewsDataSource] using [BookmarkDao].
 * 
 * This class encapsulates Room interactions and handles potential 
 * database exceptions gracefully.
 */
@Singleton
class LocalNewsDataSourceImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : LocalNewsDataSource {

    override suspend fun upsertBookmark(bookmark: BookmarkEntity) {
        try {
            bookmarkDao.upsert(bookmark)
        } catch (e: Exception) {
            Timber.e(e, "Error upserting bookmark: ${bookmark.url}")
            throw e
        }
    }

    override suspend fun deleteBookmark(bookmark: BookmarkEntity) {
        try {
            bookmarkDao.delete(bookmark)
        } catch (e: Exception) {
            Timber.e(e, "Error deleting bookmark: ${bookmark.url}")
            throw e
        }
    }

    override fun getBookmarks(): Flow<List<BookmarkEntity>> {
        return bookmarkDao.getBookmarks()
    }

    override suspend fun getBookmarkByUrl(url: String): BookmarkEntity? {
        return try {
            bookmarkDao.getBookmarkByUrl(url)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching bookmark by URL: $url")
            null
        }
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return try {
            bookmarkDao.isBookmarked(url)
        } catch (e: Exception) {
            Timber.e(e, "Error checking if bookmarked: $url")
            false
        }
    }
}
