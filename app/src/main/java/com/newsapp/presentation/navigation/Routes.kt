package com.newsapp.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.newsapp.domain.model.Article
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
     * @param article The article domain model to be displayed.
     */
    @Serializable
    data class Detail(val article: Article) : Routes

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

/**
 * Custom [NavType] for [Article] to allow passing it as a navigation argument.
 * 
 * Uses [Json] for serialization/deserialization and [Uri.encode]/[Uri.decode] 
 * to safely embed the JSON string within the navigation route URL.
 */
val ArticleNavType = object : NavType<Article>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Article? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Article {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Article): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: Article) {
        bundle.putString(key, Json.encodeToString(value))
    }
}
