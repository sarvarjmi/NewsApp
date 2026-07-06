package com.newsapp.data.repository

import com.newsapp.core.network.NetworkResult
import com.newsapp.data.local.database.NewsDatabase
import com.newsapp.data.local.datasource.LocalNewsDataSource
import com.newsapp.data.local.entity.BookmarkEntity
import com.newsapp.data.local.entity.NewsArticleEntity
import com.newsapp.data.remote.datasource.RemoteNewsDataSource
import com.newsapp.data.remote.dto.NewsResponseDto
import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import androidx.room.withTransaction

class NewsRepositoryImplTest {

    private val remoteDataSource: RemoteNewsDataSource = mockk()
    private val localDataSource: LocalNewsDataSource = mockk()
    private val database: NewsDatabase = mockk()
    
    private lateinit var repository: NewsRepositoryImpl

    private val sampleArticle = Article(
        url = "https://example.com",
        title = "Title",
        author = "Author",
        description = "Desc",
        content = "Content",
        urlToImage = null,
        publishedAt = "2024-03-20T10:00:00Z",
        source = Source(id = "id", name = "Name"),
        isBookmarked = false
    )

    @BeforeEach
    fun setup() {
        repository = NewsRepositoryImpl(remoteDataSource, localDataSource, database)
        mockkStatic("androidx.room.RoomDatabaseKt")
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `saveBookmark should call localDataSource upsertBookmark`() = runTest {
        coEvery { localDataSource.upsertBookmark(any()) } returns Unit
        
        repository.saveBookmark(sampleArticle)
        
        coVerify { localDataSource.upsertBookmark(any()) }
    }

    @Test
    fun `removeBookmark should call localDataSource deleteBookmark`() = runTest {
        coEvery { localDataSource.deleteBookmark(any()) } returns Unit
        
        repository.removeBookmark(sampleArticle)
        
        coVerify { localDataSource.deleteBookmark(any()) }
    }

    @Test
    fun `isBookmarked should return value from localDataSource`() = runTest {
        coEvery { localDataSource.isBookmarked(sampleArticle.url) } returns true
        
        val result = repository.isBookmarked(sampleArticle.url)
        
        assertThat(result).isTrue()
    }

    @Test
    fun `getArticleFromCache should check bookmarks then cache`() = runTest {
        coEvery { localDataSource.getBookmarkByUrl(sampleArticle.url) } returns null
        coEvery { localDataSource.getCachedArticleByUrl(sampleArticle.url) } returns NewsArticleEntity(
            url = sampleArticle.url,
            title = sampleArticle.title,
            author = sampleArticle.author,
            description = sampleArticle.description,
            content = sampleArticle.content,
            urlToImage = sampleArticle.urlToImage,
            publishedAt = sampleArticle.publishedAt,
            sourceId = sampleArticle.source.id,
            sourceName = sampleArticle.source.name,
            category = "general"
        )
        
        val result = repository.getArticleFromCache(sampleArticle.url)
        
        assertThat(result?.url).isEqualTo(sampleArticle.url)
        coVerify { localDataSource.getBookmarkByUrl(sampleArticle.url) }
        coVerify { localDataSource.getCachedArticleByUrl(sampleArticle.url) }
    }
}
