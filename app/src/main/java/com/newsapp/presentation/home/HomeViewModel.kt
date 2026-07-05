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
 * Coordinates between the domain layer use cases and the Home UI.
 * Implements a Unidirectional Data Flow (UDF) by exposing a single [state] 
 * and handling [HomeEvent]s.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<HomeUiState, HomeSideEffect>(HomeUiState()) {

    /**
     * A reactive stream of paginated articles.
     * 
     * [flatMapLatest]: Restarts the paging stream whenever the category changes.
     * [cachedIn]: Preserves the paging data in the ViewModel's scope so it 
     *             survives configuration changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val articles: Flow<PagingData<Article>> = state
        .flatMapLatest { currentState ->
            getTopHeadlinesUseCase(currentState.selectedCategory.lowercase())
        }
        .cachedIn(viewModelScope)

    /**
     * Main entry point for user interactions.
     *
     * Business logic is kept within this method or delegated to use cases, 
     * ensuring the Composable remains pure and stateless.
     */
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnCategorySelected -> {
                if (currentState.selectedCategory == event.category) return
                updateState { it.copy(selectedCategory = event.category) }
            }
            
            is HomeEvent.OnBookmarkToggled -> {
                // Background operation for database interaction
                safeLaunch {
                    toggleBookmarkUseCase(event.article)
                }
            }
            
            is HomeEvent.OnArticleClicked -> {
                // Navigation is handled via side effects to ensure one-time delivery
                sendEffect(HomeSideEffect.NavigateToDetail(event.article.url))
            }
            
            HomeEvent.OnSearchClicked -> {
                sendEffect(HomeSideEffect.NavigateToSearch)
            }
            
            HomeEvent.OnRefresh -> {
                // Actual refresh is triggered on the PagingItems in Compose
            }
            
            HomeEvent.OnRetry -> {
                // Actual retry is triggered on the PagingItems in Compose
            }
        }
    }
}
