package com.newsapp.presentation.home

import com.newsapp.domain.model.Article

/**
 * One-time side effects for the Home screen (e.g., Navigation).
 */
sealed interface HomeSideEffect {
    data class NavigateToDetail(val article: Article) : HomeSideEffect
    data object NavigateToSearch : HomeSideEffect
}
