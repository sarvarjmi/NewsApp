package com.newsapp.data.remote.paging

import androidx.paging.PagingSource
import com.newsapp.data.remote.datasource.RemoteNewsDataSource
import com.newsapp.data.remote.dto.NewsResponseDto
import com.newsapp.util.TestData
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NewsPagingSourceTest {

    private val remoteDataSource: RemoteNewsDataSource = mockk()
    private lateinit var pagingSource: NewsPagingSource

    @BeforeEach
    fun setup() {
        pagingSource = NewsPagingSource(
            remoteDataSource = remoteDataSource,
            isBookmarked = { false },
            category = "general"
        )
    }

    @Test
    fun `load should return success load result when data is fetched`() = runTest {
        val articleDto = TestData.sampleArticleDto
        val response = NewsResponseDto(
            status = "ok",
            totalResults = 1,
            articles = listOf(articleDto)
        )
        
        coEvery { 
            remoteDataSource.getTopHeadlines("general", any(), any()) 
        } returns com.newsapp.core.network.NetworkResult.Success(response)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class.java)
        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(1)
        assertThat(page.data[0].url).isEqualTo(articleDto.url)
    }

    @Test
    fun `load should return error load result when fetching fails`() = runTest {
        val exception = Exception("Network error")
        coEvery { 
            remoteDataSource.getTopHeadlines("general", any(), any()) 
        } returns com.newsapp.core.network.NetworkResult.Error("Error", exception)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
        val errorResult = result as PagingSource.LoadResult.Error
        assertThat(errorResult.throwable).isEqualTo(exception)
    }
}
