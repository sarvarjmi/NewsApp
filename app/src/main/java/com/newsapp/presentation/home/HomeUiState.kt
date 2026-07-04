package com.newsapp.presentation.home

/**
 * UI State for the Home screen.
 * Paging-specific states (loading, error, list) are managed by PagingData.
 */
data class HomeUiState(
    val selectedCategory: String = "General",
    val categories: List<String> = listOf(
        "General", "Business", "Technology", "Sports", 
        "Health", "Science", "Entertainment"
    )
)
