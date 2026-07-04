package com.newsapp.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.newsapp.domain.model.Article
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.domain.usecase.news.GetTopHeadlinesUseCase
import com.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * ViewModel for the Home Screen.
 * 
 * Manages the state of the top headlines feed, category selection, 
 * and bookmark interactions.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<HomeUiState, Unit>(HomeUiState()) {

    /**
     * A reactive stream of paginated articles.
     * 
     * We use [flatMapLatest] to automatically restart the paging flow whenever 
     * the [state.selectedCategory] changes.
     * [cachedIn(viewModelScope)] ensures that the paging state is preserved 
     * during configuration changes like screen rotation.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val articles: Flow<PagingData<Article>> = state
        .flatMapLatest { currentState ->
            getTopHeadlinesUseCase(currentState.selectedCategory.lowercase())
        }
        .cachedIn(viewModelScope)

    /**
     * Entry point for all UI interactions.
     * 
     * Decouples the UI from the implementation details of the ViewModel.
     */
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnCategorySelected -> updateCategory(event.category)
            is HomeEvent.OnBookmarkToggled -> toggleBookmark(event.article)
            HomeEvent.OnRefresh -> { /* Paging handles refresh via UI */ }
            HomeEvent.OnRetry -> { /* Paging handles retry via UI */ }
        }
    }

    /**
     * Updates the UI state with a new category.
     * This triggers the [articles] flow to emit a new PagingData stream.
     */
    private fun updateCategory(category: String) {
        if (currentState.selectedCategory == category) return
        updateState { it.copy(selectedCategory = category) }
    }

    /**
     * Persists or removes an article from the local database.
     * 
     * Uses [safeLaunch] from [BaseViewModel] to handle potential database errors.
     */
    private fun toggleBookmark(article: Article) {
        safeLaunch {
            toggleBookmarkUseCase(article)
        }
    }
}
