package com.newsapp.presentation.home

import com.newsapp.domain.model.Article

/**
 * Events sent from the UI to the [HomeViewModel].
 */
sealed interface HomeEvent {
    data class OnCategorySelected(val category: String) : HomeEvent
    data class OnBookmarkToggled(val article: Article) : HomeEvent
    data class OnArticleClicked(val article: Article) : HomeEvent
    data object OnSearchClicked : HomeEvent
    data object OnRefresh : HomeEvent
    data object OnRetry : HomeEvent
}
