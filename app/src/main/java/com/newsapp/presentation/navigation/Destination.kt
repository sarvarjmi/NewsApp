package com.newsapp.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.newsapp.R

/**
 * Sealed class representing the top-level destinations in the application.
 * 
 * Each destination contains metadata for the Bottom Navigation Bar, 
 * including icons, labels (string resources), and its associated route.
 */
sealed class Destination(
    val route: Routes,
    @StringRes val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val baseUri: String? = null
) {
    /**
     * Home Destination: The landing page showing top headlines.
     */
    data object Home : Destination(
        route = Routes.Home,
        labelRes = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        baseUri = "newsapp://home"
    )

    /**
     * Search Destination: The search interface for articles.
     */
    data object Search : Destination(
        route = Routes.Search,
        labelRes = R.string.search,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        baseUri = "newsapp://search"
    )

    /**
     * Bookmarks Destination: Offline saved articles.
     */
    data object Bookmarks : Destination(
        route = Routes.Bookmarks,
        labelRes = R.string.bookmarks,
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder,
        baseUri = "newsapp://bookmarks"
    )

    companion object {
        /**
         * List of all top-level destinations to be displayed in the Navigation Bar.
         */
        val topLevelDestinations = listOf(Home, Search, Bookmarks)

        /**
         * Resolves a [Destination] from a given [Routes].
         */
        fun fromRoute(route: Routes): Destination? {
            return when (route) {
                is Routes.Home -> Home
                is Routes.Search -> Search
                is Routes.Bookmarks -> Bookmarks
                else -> null
            }
        }
    }
}
