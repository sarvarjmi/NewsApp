package com.newsapp.presentation.bookmarks

import com.newsapp.domain.model.Article

/**
 * Immutable UI state model for the Bookmark Screen.
 *
 * This class follows the Unidirectional Data Flow (UDF) pattern, providing the
 * UI with everything it needs to render the list of saved articles in one atomic object.
 *
 * @property isLoading Indicates if the initial data fetch from the local database is in progress.
 *                     Typically shows a full-screen shimmer or loading indicator.
 * @property isRefreshing Indicates if a manual pull-to-refresh operation is active.
 *                        Unlike [isLoading], this usually shows a smaller indicator while keeping
 *                        the current list visible.
 * @property articles The list of [Article] domain models that have been saved by the user.
 * @property bookmarkCount The total number of saved articles. This is provided for direct 
 *                        binding to counters in the UI (e.g., in the Top App Bar).
 * @property articleToDelete The article currently selected for deletion. If not null, the UI 
 *                          should display a confirmation dialog.
 * @property error A user-friendly error message if the database query or any bookmark 
 *                 action (like deletion) fails.
 */
data class BookmarkUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<Article> = emptyList(),
    val bookmarkCount: Int = 0,
    val articleToDelete: Article? = null,
    val error: String? = null
) {
    /**
     * Determines if the state represents a successfully loaded list with content.
     */
    val isSuccess: Boolean get() = !isLoading && error == null && articles.isNotEmpty()

    /**
     * Determines if the state represents an empty bookmark list.
     * This flag can be used to show specialized illustrations and "Discover" calls to action.
     */
    val isEmpty: Boolean get() = !isLoading && error == null && articles.isEmpty()

    /**
     * Static factory to provide the starting state for the Bookmark Screen.
     */
    companion object {
        fun initial() = BookmarkUiState(isLoading = true)
    }
}
