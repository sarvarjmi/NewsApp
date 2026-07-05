package com.newsapp.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Centralized route definitions for NewsApp using Jetpack Compose Navigation type-safety.
 *
 * Each destination is represented as a [Serializable] object or class, enabling
 * compile-time safety for route definitions and argument passing.
 */
sealed interface Routes {

    /**
     * Nested graph for the main bottom navigation destinations.
     */
    @Serializable
    data object MainGraph : Routes

    /**
     * The Home screen.
     * Displays a curated feed of top headlines with category filtering.
     */
    @Serializable
    data object Home : Routes

    /**
     * The Search screen.
     * Provides a debounced search interface to explore articles by keywords.
     */
    @Serializable
    data object Search : Routes

    /**
     * The Bookmarks screen.
     * Lists all articles saved by the user for offline access.
     */
    @Serializable
    data object Bookmarks : Routes

    /**
     * The Article Detail screen.
     * Renders the full details of a specific news article.
     *
     * @param articleUrl The unique canonical URL of the article, used to fetch or identify the content.
     */
    @Serializable
    data class Detail(val articleUrl: String) : Routes

    /**
     * The WebView screen.
     * An optional destination used to display the original news source webpage
     * within the application context.
     *
     * @param url The direct web link to the news source.
     */
    @Serializable
    data class WebView(val url: String) : Routes
}
