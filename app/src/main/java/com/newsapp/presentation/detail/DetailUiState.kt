package com.newsapp.presentation.detail

import com.newsapp.domain.model.Article

/**
 * UI State for the Article Detail screen.
 * 
 * @property isLoading Indicates if the article content or bookmark status is being loaded.
 * @property article The specific article data to be displayed.
 * @property isBookmarked Current bookmark status of this article in the local DB.
 * @property error An optional error message if loading the article fails.
 */
data class DetailUiState(
    val isLoading: Boolean = false,
    val article: Article? = null,
    val isBookmarked: Boolean = false,
    val error: String? = null
)
