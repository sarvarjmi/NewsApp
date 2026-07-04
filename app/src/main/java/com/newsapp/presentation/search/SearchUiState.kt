package com.newsapp.presentation.search

/**
 * UI State for the Search screen.
 * 
 * Manages the current search query. Similar to the Home screen, the 
 * paginated search results and their loading/error states are handled 
 * by the Paging 3 library.
 *
 * @property searchQuery The current text entered by the user in the search bar.
 */
data class SearchUiState(
    val searchQuery: String = ""
)
