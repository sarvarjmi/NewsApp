package com.newsapp.presentation.search

import com.newsapp.domain.model.Article

/**
 * One-time side effects for the Search screen.
 */
sealed interface SearchSideEffect {
    /**
     * Navigate to the details of a specific article.
     */
    data class NavigateToDetail(val article: Article) : SearchSideEffect
}
