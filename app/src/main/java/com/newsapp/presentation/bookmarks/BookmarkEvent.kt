package com.newsapp.presentation.bookmarks

import com.newsapp.domain.model.Article

/**
 * UI Events for the Bookmark Screen.
 */
sealed interface BookmarkEvent {
    /**
     * Triggered when the user wants to remove a bookmark.
     * In this app, we show a confirmation dialog first.
     */
    data class OnDeleteIntent(val article: Article) : BookmarkEvent

    /**
     * Confirms the deletion after the user agrees in the dialog.
     */
    data object OnConfirmDelete : BookmarkEvent

    /**
     * Cancels the deletion and hides the dialog.
     */
    data object OnDismissDeleteDialog : BookmarkEvent

    /**
     * Triggered when the user wants to undo a recent removal.
     */
    data class OnUndoRemove(val article: Article) : BookmarkEvent

    /**
     * Triggered when the user clicks on an article to view details.
     */
    data class OnArticleClicked(val article: Article) : BookmarkEvent

    /**
     * Triggered when the user performs a pull-to-refresh.
     */
    data object OnRefresh : BookmarkEvent

    /**
     * Triggered when the user taps the retry button on an error state.
     */
    data object OnRetryClicked : BookmarkEvent
}
