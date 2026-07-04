package com.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.newsapp.domain.model.Article
import com.newsapp.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for the Home screen, responsible for managing paginated news data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    /**
     * A reactive stream of paginated articles.
     * We use [flatMapLatest] to automatically restart the paging stream 
     * whenever the selected category changes in the UI state.
     * [cachedIn] ensures the paging state is preserved across configuration changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val newsArticles: Flow<PagingData<Article>> = _state
        .flatMapLatest { currentState ->
            getTopHeadlinesUseCase(currentState.selectedCategory.lowercase())
        }
        .cachedIn(viewModelScope)

    /**
     * Updates the selected category and triggers a data refresh.
     */
    fun onCategorySelected(category: String) {
        if (_state.value.selectedCategory == category) return
        _state.update { it.copy(selectedCategory = category) }
    }
}
