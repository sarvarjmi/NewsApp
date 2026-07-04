package com.newsapp.presentation.bookmarks

import com.newsapp.domain.model.Article

data class BookmarksUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
