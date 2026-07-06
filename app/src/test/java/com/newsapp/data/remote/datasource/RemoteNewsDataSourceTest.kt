package com.newsapp.data.remote.datasource

import com.newsapp.core.dispatcher.DispatcherProvider
import com.newsapp.core.error.ErrorMapper
import com.newsapp.data.remote.api.NewsApiService
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import kotlinx.coroutines.Dispatchers

class RemoteNewsDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: NewsApiService
    private lateinit var dataSource: RemoteNewsDataSource
    private val errorMapper: ErrorMapper = ErrorMapper()
    private val dispatcherProvider: DispatcherProvider = mockk()

    private val json = Json { ignoreUnknownKeys = true }

    @BeforeEach
    fun setup() {
        every { dispatcherProvider.io } returns Dispatchers.Unconfined

        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(NewsApiService::class.java)
        
        dataSource = RemoteNewsDataSourceImpl(apiService, errorMapper, dispatcherProvider)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getTopHeadlines should return success when API returns 200`() = runTest {
        // Arrange
        val responseBody = """
            {
                "status": "ok",
                "totalResults": 1,
                "articles": [
                    {
                        "title": "Test Title",
                        "url": "https://example.com",
                        "source": { "id": "id", "name": "Name" },
                        "publishedAt": "now"
                    }
                ]
            }
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // Act
        val result = dataSource.getTopHeadlines("general", 1, 20)

        // Assert
        assertThat(result).isInstanceOf(com.newsapp.core.network.NetworkResult.Success::class.java)
        val successResult = result as com.newsapp.core.network.NetworkResult.Success
        assertThat(successResult.data.articles[0].title).isEqualTo("Test Title")
    }
}
