package com.newsapp.presentation.search

/**
 * One-time side effects for the Search screen.
 */
sealed interface SearchSideEffect {
    /**
     * Navigate to the details of a specific article.
     */
    data class NavigateToDetail(val url: String) : SearchSideEffect
}
