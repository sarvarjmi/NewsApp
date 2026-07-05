package com.newsapp.presentation.home

import androidx.compose.runtime.Immutable

/**
 * Immutable UI state model for the Home screen.
 *
 * This class serves as the single source of truth for the Home screen UI, following 
 * the Unidirectional Data Flow (UDF) pattern. 
 * 
 * We use @Immutable to explicitly tell the Compose compiler that this class's properties 
 * will not change after creation, enabling better skipping of recompositions.
 */
@Immutable
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
     */
    val isSuccess: Boolean get() = !isLoading && error == null

    /**
     * Extension property to check if the UI should represent an "Empty" state.
     */
    val isEmpty: Boolean get() = !isLoading && error == null && selectedCategory.isEmpty()
}
