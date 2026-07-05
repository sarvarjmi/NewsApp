package com.newsapp.presentation.webview

/**
 * Side Effects for the WebView Screen.
 */
sealed interface WebViewSideEffect {
    data object NavigateBack : WebViewSideEffect
    data class ShareUrl(val url: String, val title: String) : WebViewSideEffect
    data class OpenInExternalBrowser(val url: String) : WebViewSideEffect
}
