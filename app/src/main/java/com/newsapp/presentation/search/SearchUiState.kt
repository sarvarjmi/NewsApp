package com.newsapp.presentation.search

import com.newsapp.domain.model.Article

data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null
)
