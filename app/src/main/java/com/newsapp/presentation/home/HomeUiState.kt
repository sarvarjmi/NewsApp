package com.newsapp.presentation.home

import com.newsapp.domain.model.Article

data class HomeUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null,
    val selectedCategory: String = "All"
)
