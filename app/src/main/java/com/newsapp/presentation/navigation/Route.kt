package com.newsapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Bookmarks : Route

    @Serializable
    data class Detail(val articleUrl: String) : Route
}
