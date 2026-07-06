package com.newsapp.domain.usecase.bookmark

import com.newsapp.domain.repository.NewsRepository
import com.newsapp.util.TestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ToggleBookmarkUseCaseTest {

    private val repository: NewsRepository = mockk()
    private lateinit var useCase: ToggleBookmarkUseCase

    @BeforeEach
    fun setup() {
        useCase = ToggleBookmarkUseCase(repository)
    }

    @Test
    fun `invoke should delegate to repository toggleBookmark`() = runTest {
        // Arrange
        val article = TestData.sampleArticle
        coEvery { repository.toggleBookmark(article) } returns Unit

        // Act
        useCase(article)

        // Assert
        coVerify { repository.toggleBookmark(article) }
    }
}
