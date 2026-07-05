package com.newsapp.presentation.detail

import com.newsapp.domain.model.Article

/**
 * Represents the immutable UI state for the Article Detail screen.
 *
 * This class follows the Unidirectional Data Flow (UDF) pattern, providing the
 * UI with everything it needs to render the detail view in one atomic object.
 *
 * @property isLoading Indicates whether the article data is currently being fetched
 *                     from the repository or database. When true, the UI should
 *                     display a shimmer or loading indicator.
 * @property article The [Article] domain model containing the news content, 
 *                   headlines, and metadata. Will be null until a successful 
 *                   load occurs.
 * @property isBookmarked Current persistence status of the article. If true, the
 *                        article exists in the local "bookmarks" database. This 
 *                        is used to drive the state of the bookmark toggle icon.
 * @property readingTime An estimated time (e.g., "5 min read") calculated based 
 *                       on the article's word count. 
 * @property error A user-friendly error message if the article cannot be loaded
 *                 or if a specific action (like sharing) fails. If null, no
 *                 error is present.
 */
data class DetailUiState(
    val isLoading: Boolean = false,
    val article: Article? = null,
    val isBookmarked: Boolean = false,
    val readingTime: String? = null,
    val error: String? = null
) {
    /**
     * Helper property to determine if the article has been successfully loaded.
     */
    val isSuccess: Boolean get() = !isLoading && article != null && error == null

    /**
     * Helper property to determine if the UI should show an error screen.
     */
    val hasError: Boolean get() = error != null && article == null

    /**
     * Static factory for creating an initial/empty state.
     */
    companion object {
        fun initial() = DetailUiState(isLoading = true)
    }
}
