package com.newsapp.presentation.bookmarks

import com.newsapp.domain.model.Article

/**
 * One-time side effects for the Bookmark Screen.
 */
sealed interface BookmarkSideEffect {
    /**
     * Navigate to the details of a specific article.
     */
    data class NavigateToDetail(val url: String) : BookmarkSideEffect

    /**
     * Show a Snackbar with an undo option after removing a bookmark.
     */
    data class ShowUndoSnackbar(val article: Article) : BookmarkSideEffect
}
