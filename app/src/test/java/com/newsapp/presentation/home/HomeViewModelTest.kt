package com.newsapp.presentation.home

import app.cash.turbine.test
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.domain.usecase.news.GetTopHeadlinesUseCase
import com.newsapp.util.MainDispatcherRule
import com.newsapp.util.TestData
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()

    @RegisterExtension
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setup() {
        every { getTopHeadlinesUseCase(any()) } returns flowOf()
        viewModel = HomeViewModel(getTopHeadlinesUseCase, toggleBookmarkUseCase)
    }

    @Test
    fun `OnCategorySelected should update state when different category selected`() = runTest {
        val newCategory = "Business"
        
        viewModel.state.test {
            // Initial state
            assertThat(awaitItem().selectedCategory).isEqualTo("General")
            
            viewModel.onEvent(HomeEvent.OnCategorySelected(newCategory))
            
            assertThat(awaitItem().selectedCategory).isEqualTo(newCategory)
        }
    }

    @Test
    fun `OnCategorySelected should NOT update state when same category selected`() = runTest {
        viewModel.onEvent(HomeEvent.OnCategorySelected("General"))
        
        viewModel.state.test {
            assertThat(awaitItem().selectedCategory).isEqualTo("General")
            expectNoEvents()
        }
    }

    @Test
    fun `OnArticleClicked should send NavigateToDetail effect`() = runTest {
        val article = TestData.sampleArticle
        viewModel.effect.test {
            viewModel.onEvent(HomeEvent.OnArticleClicked(article))
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(HomeSideEffect.NavigateToDetail::class.java)
            assertThat((effect as HomeSideEffect.NavigateToDetail).article).isEqualTo(article)
        }
    }

    @Test
    fun `OnSearchClicked should send NavigateToSearch effect`() = runTest {
        viewModel.effect.test {
            viewModel.onEvent(HomeEvent.OnSearchClicked)
            assertThat(awaitItem()).isInstanceOf(HomeSideEffect.NavigateToSearch::class.java)
        }
    }
}
