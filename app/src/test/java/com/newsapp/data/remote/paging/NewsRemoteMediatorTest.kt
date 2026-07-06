package com.newsapp.data.remote.paging

import androidx.paging.*
import com.newsapp.data.local.database.NewsDatabase
import com.newsapp.data.remote.datasource.RemoteNewsDataSource
import com.newsapp.data.remote.dto.NewsResponseDto
import com.newsapp.util.TestData
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import androidx.room.withTransaction

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class NewsRemoteMediatorTest {

    private val remoteDataSource: RemoteNewsDataSource = mockk()
    private val database: NewsDatabase = mockk()
    private lateinit var mediator: NewsRemoteMediator

    @BeforeEach
    fun setup() {
        mediator = NewsRemoteMediator(remoteDataSource, database, "general")
        mockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `refresh load should return success when remote data is fetched`() = runTest {
        val articleDto = TestData.sampleArticleDto
        val response = NewsResponseDto(
            status = "ok",
            totalResults = 1,
            articles = listOf(articleDto)
        )
        
        coEvery { 
            remoteDataSource.getTopHeadlines("general", any(), any()) 
        } returns com.newsapp.core.network.NetworkResult.Success(response)
        
        // Mocking database transaction and DAOs
        coEvery { database.withTransaction(any<suspend () -> Any>()) } coAnswers {
            (args[0] as suspend () -> Any).invoke()
        }
        every { database.newsRemoteKeysDao } returns mockk(relaxed = true)
        every { database.newsArticleDao } returns mockk(relaxed = true)

        val pagingState = PagingState<Int, com.newsapp.data.local.entity.NewsArticleEntity>(
            listOf(),
            null,
            PagingConfig(20),
            10
        )
        
        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
    }
}
