package com.newsapp.presentation.home

/**
 * One-time side effects for the Home screen (e.g., Navigation).
 */
sealed interface HomeSideEffect {
    data class NavigateToDetail(val url: String) : HomeSideEffect
    data object NavigateToSearch : HomeSideEffect
}
