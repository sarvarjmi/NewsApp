package com.newsapp.presentation.bookmarks

import com.newsapp.domain.model.Article

/**
 * UI State for the Bookmarks screen.
 * 
 * Unlike Home and Search, Bookmarks are typically loaded from a local 
 * database as a full list (non-paginated for initial MVP).
 *
 * @property isLoading Indicates if the bookmarks are currently being fetched from DB.
 * @property articles The list of articles saved by the user.
 * @property error An optional error message if the database operation fails.
 */
data class BookmarkUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null
)
