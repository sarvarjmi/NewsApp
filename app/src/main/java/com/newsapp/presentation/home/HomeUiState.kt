package com.newsapp.presentation.home

/**
 * UI State for the Home screen.
 * 
 * Note: Paginated article data and its associated states (Loading, Error, Empty) 
 * are managed reactively via PagingData in the ViewModel. This state class 
 * handles additional UI metadata like category selection.
 *
 * @property selectedCategory The currently active news category.
 * @property categories The list of available categories for the user to filter.
 */
data class HomeUiState(
    val selectedCategory: String = "General",
    val categories: List<String> = listOf(
        "General", "Business", "Technology", "Sports", 
        "Health", "Science", "Entertainment"
    )
)
