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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : BaseViewModel<SearchUiState, Unit>(SearchUiState()) {

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: Flow<PagingData<Article>> = state
        .flatMapLatest { currentState ->
            if (currentState.searchQuery.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                searchNewsUseCase(currentState.searchQuery)
            }
        }
        .cachedIn(viewModelScope)

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChanged -> {
                updateState { it.copy(searchQuery = event.query) }
            }
            is SearchEvent.OnBookmarkToggled -> {
                safeLaunch {
                    toggleBookmarkUseCase(event.article)
                }
            }
        }
    }
}
