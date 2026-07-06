package com.newsapp.presentation.search

import app.cash.turbine.test
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.domain.usecase.search.SearchNewsUseCase
import com.newsapp.util.MainDispatcherRule
import com.newsapp.util.TestData
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val searchNewsUseCase: SearchNewsUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()

    @RegisterExtension
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchViewModel

    @BeforeEach
    fun setup() {
        viewModel = SearchViewModel(searchNewsUseCase, toggleBookmarkUseCase)
    }

    @Test
    fun `OnQueryChanged should update state immediately`() = runTest {
        val query = "android"
        
        viewModel.state.test {
            assertThat(awaitItem().searchQuery).isEmpty()
            
            viewModel.onEvent(SearchEvent.OnQueryChanged(query))
            
            val state = awaitItem()
            assertThat(state.searchQuery).isEqualTo(query)
            assertThat(state.isInitialLoad).isFalse()
        }
    }

    @Test
    fun `initQuery should update state if query is different`() = runTest {
        val query = "kotlin"
        
        viewModel.state.test {
            awaitItem()
            
            viewModel.initQuery(query)
            
            assertThat(awaitItem().searchQuery).isEqualTo(query)
        }
    }

    @Test
    fun `OnArticleClicked should send NavigateToDetail effect`() = runTest {
        val article = TestData.sampleArticle
        viewModel.effect.test {
            viewModel.onEvent(SearchEvent.OnArticleClicked(article))
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(SearchSideEffect.NavigateToDetail::class.java)
            assertThat((effect as SearchSideEffect.NavigateToDetail).article).isEqualTo(article)
        }
    }
}
