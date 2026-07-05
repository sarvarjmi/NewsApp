package com.newsapp.presentation.search

/**
 * Immutable UI state model for the Search screen.
 *
 * This class serves as the single source of truth for the Search screen's metadata, 
 * following the Unidirectional Data Flow (UDF) pattern.
 *
 * Architectural Note:
 * This model does NOT contain the actual list of search results. In modern Android
 * development using Paging 3, [androidx.paging.PagingData] is managed as a separate
 * Flow in the ViewModel. This ensures that the UI list remains highly performant 
 * and preserves its own scroll state independent of other UI metadata changes.
 *
 * @property searchQuery The current text present in the search input field.
 * @property searchHistory A list of previously successful search queries retrieved from local storage.
 * @property isLoading Indicates if a global background operation is occurring (e.g., initial history load).
 * @property isSearchActive Whether the screen should display search results instead of search history.
 * @property error An optional user-friendly error message for non-paging related failures.
 */
data class SearchUiState(
    val searchQuery: String = "",
    val searchHistory: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSearchActive: Boolean = false,
    val isInitialLoad: Boolean = true,
    val error: String? = null
) {
    /**
     * Determines if the screen is in its "Idle" or "Initial" state.
     * True if the query is blank and there is no previous search history to show.
     */
    val isIdle: Boolean get() = searchQuery.isBlank() && searchHistory.isEmpty()

    /**
     * Determines if the search history list should be presented to the user.
     * History is shown when the user is not actively viewing results and has saved queries.
     */
    val showHistory: Boolean get() = !isSearchActive && searchHistory.isNotEmpty()

    /**
     * Determines if the UI should represent a generic failure state.
     */
    val hasError: Boolean get() = error != null
}
