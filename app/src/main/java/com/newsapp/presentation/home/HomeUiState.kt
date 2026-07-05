package com.newsapp.presentation.home

/**
 * Immutable UI state model for the Home screen.
 *
 * This class serves as the single source of truth for the Home screen UI, following 
 * the Unidirectional Data Flow (UDF) pattern. 
 *
 * Architectural Note:
 * In this application, we use the Paging 3 library. While Paging 3 manages its own 
 * internal states (Loading, Error, Success) for the list itself, this [HomeUiState] 
 * manages the metadata and global UI configurations that surround the list.
 *
 * @property selectedCategory The news category currently selected by the user (e.g., "Technology").
 *                            Updating this property triggers the ViewModel to restart the 
 *                            paging stream with a new filter.
 * @property categories The collection of available categories displayed in the filter chips.
 * @property isLoading Indicates if a global loading operation is occurring (e.g., during 
 *                      the first ever app launch or category transition).
 * @property isRefreshing Tracks the state of a manual pull-to-refresh action.
 * @property error A user-friendly error message if a non-paging operation fails or if
 *                 a global error occurs that should be displayed via a Snackbar or Banner.
 * @property searchQuery A temporary holder for search intent if triggered from the home top bar.
 */
data class HomeUiState(
    val selectedCategory: String = "General",
    val categories: List<String> = listOf(
        "General", 
        "Business", 
        "Technology", 
        "Sports", 
        "Health", 
        "Science", 
        "Entertainment"
    ),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
) {
    /**
     * Extension property to check if the state is considered "Success".
     * True if there is no active error and global loading has finished.
     */
    val isSuccess: Boolean get() = !isLoading && error == null

    /**
     * Extension property to check if the UI should represent an "Empty" state.
     * Note: Actual list emptiness is usually determined by checking PagingItems.itemCount.
     */
    val isEmpty: Boolean get() = !isLoading && error == null && selectedCategory.isEmpty()
}
