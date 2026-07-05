package com.newsapp.presentation.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.newsapp.domain.model.Article
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.domain.usecase.search.SearchNewsUseCase
import com.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * ViewModel for the Search Screen.
 * 
 * It handles debounced search queries, pagination of results, and bookmark toggling.
 * Implements Unidirectional Data Flow (UDF) using [SearchUiState] and [SearchEvent].
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<SearchUiState, SearchSideEffect>(SearchUiState()) {

    /**
     * A reactive stream of paginated search results.
     * 
     * Pipeline logic:
     * 1. Extract [searchQuery] from the UI state.
     * 2. [debounce(500)]: Wait for the user to stop typing for 500ms.
     * 3. [distinctUntilChanged]: Only trigger a new search if the query actually changed.
     * 4. [flatMapLatest]: Cancel the previous search and start a new one with the latest query.
     * 5. [cachedIn]: Persist results in memory for orientation changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    val searchResults: Flow<PagingData<Article>> = state
        .map { it.searchQuery }
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                searchNewsUseCase(query)
            }
        }
        .cachedIn(viewModelScope)

    /**
     * Centralized event handler for all user interactions on the Search screen.
     */
    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChanged -> {
                // Update the state immediately so the SearchBar is responsive
                updateState { it.copy(searchQuery = event.query) }
            }
            
            is SearchEvent.OnBookmarkToggled -> {
                // Toggle bookmark status in the background database
                safeLaunch {
                    toggleBookmarkUseCase(event.article)
                }
            }
            
            is SearchEvent.OnArticleClicked -> {
                sendEffect(SearchSideEffect.NavigateToDetail(event.url))
            }
        }
    }
}
