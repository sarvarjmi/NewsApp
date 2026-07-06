package com.newsapp.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.newsapp.data.local.database.NewsDatabase
import com.newsapp.data.local.entity.BookmarkEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookmarkDaoTest {

    private lateinit var database: NewsDatabase
    private lateinit var bookmarkDao: BookmarkDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        bookmarkDao = database.bookmarkDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsert_shouldInsertBookmark() = runBlocking {
        val bookmark = createBookmark("https://example.com/1")
        bookmarkDao.upsert(bookmark)

        val result = bookmarkDao.getBookmarks().first()
        assertThat(result).hasSize(1)
        assertThat(result[0].url).isEqualTo(bookmark.url)
    }

    @Test
    fun delete_shouldRemoveBookmark() = runBlocking {
        val bookmark = createBookmark("https://example.com/1")
        bookmarkDao.upsert(bookmark)
        bookmarkDao.delete(bookmark)

        val result = bookmarkDao.getBookmarks().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun isBookmarked_shouldReturnTrueIfExits() = runBlocking {
        val url = "https://example.com/1"
        val bookmark = createBookmark(url)
        bookmarkDao.upsert(bookmark)

        val exists = bookmarkDao.isBookmarked(url)
        assertThat(exists).isTrue()
    }

    private fun createBookmark(url: String) = BookmarkEntity(
        url = url,
        title = "Title",
        author = "Author",
        description = "Desc",
        content = "Content",
        urlToImage = null,
        publishedAt = "2024-03-20T10:00:00Z",
        sourceId = "id",
        sourceName = "Name",
        bookmarkedAt = System.currentTimeMillis()
    )
}
