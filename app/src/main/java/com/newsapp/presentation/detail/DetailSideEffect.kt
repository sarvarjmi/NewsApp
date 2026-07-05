package com.newsapp.presentation.detail

/**
 * One-time side effects for the Article Detail screen.
 */
sealed interface DetailSideEffect {
    /**
     * Navigate to the internal WebView screen.
     */
    data class NavigateToWebView(val url: String) : DetailSideEffect

    /**
     * Trigger the system share sheet.
     */
    data class ShareArticle(val url: String, val title: String) : DetailSideEffect

    /**
     * Launch Chrome Custom Tabs or system browser.
     */
    data class OpenBrowser(val url: String) : DetailSideEffect

    /**
     * Navigate back to the previous screen.
     */
    data object NavigateBack : DetailSideEffect
}
