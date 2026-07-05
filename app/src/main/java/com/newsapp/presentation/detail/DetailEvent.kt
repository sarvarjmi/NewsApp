package com.newsapp.presentation.detail

import com.newsapp.domain.model.Article

/**
 * UI Events for the Article Detail screen.
 */
sealed interface DetailEvent {
    /**
     * Triggered when the user wants to bookmark or remove the article.
     */
    data class OnBookmarkToggled(val article: Article) : DetailEvent

    /**
     * Triggered when the user wants to share the article.
     */
    data class OnShareClicked(val article: Article) : DetailEvent

    /**
     * Triggered when the user wants to open the original article in a browser.
     */
    data class OnOpenInBrowserClicked(val url: String) : DetailEvent

    /**
     * Triggered when the user wants to retry loading the article.
     */
    data object OnRetryClicked : DetailEvent

    /**
     * Triggered when the user navigates back.
     */
    data object OnBackClicked : DetailEvent
}
