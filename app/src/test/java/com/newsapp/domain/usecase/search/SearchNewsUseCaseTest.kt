package com.newsapp.domain.usecase.search

import androidx.paging.PagingData
import com.newsapp.domain.repository.NewsRepository
import com.newsapp.util.TestData
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchNewsUseCaseTest {

    private val repository: NewsRepository = mockk()
    private lateinit var useCase: SearchNewsUseCase

    @BeforeEach
    fun setup() {
        useCase = SearchNewsUseCase(repository)
    }

    @Test
    fun `invoke with blank query should return empty PagingData flow and not call repository`() = runTest {
        useCase("")
        
        verify(exactly = 0) { repository.searchNews(any()) }
    }

    @Test
    fun `invoke with valid query should call repository searchNews`() = runTest {
        val query = "android"
        val expectedFlow = flowOf(PagingData.empty<com.newsapp.domain.model.Article>())
        every { repository.searchNews(query) } returns expectedFlow
        
        val result = useCase(query)
        
        assertThat(result).isEqualTo(expectedFlow)
        verify { repository.searchNews(query) }
    }
}
