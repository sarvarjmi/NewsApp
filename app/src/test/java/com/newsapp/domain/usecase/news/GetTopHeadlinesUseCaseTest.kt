package com.newsapp.domain.usecase.news

import androidx.paging.PagingData
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTopHeadlinesUseCaseTest {

    private val repository: NewsRepository = mockk()
    private lateinit var useCase: GetTopHeadlinesUseCase

    @BeforeEach
    fun setup() {
        useCase = GetTopHeadlinesUseCase(repository)
    }

    @Test
    fun `invoke should delegate to repository getTopHeadlines`() {
        // Arrange
        val category = "technology"
        val expectedFlow = flowOf(PagingData.empty<Article>())
        every { repository.getTopHeadlines(category) } returns expectedFlow

        // Act
        val result = useCase(category)

        // Assert
        assertThat(result).isEqualTo(expectedFlow)
        verify { repository.getTopHeadlines(category) }
    }

    @Test
    fun `invoke with null category should call repository with null`() {
        // Arrange
        val expectedFlow = flowOf(PagingData.empty<Article>())
        every { repository.getTopHeadlines(null) } returns expectedFlow

        // Act
        val result = useCase(null)

        // Assert
        assertThat(result).isEqualTo(expectedFlow)
        verify { repository.getTopHeadlines(null) }
    }
}
