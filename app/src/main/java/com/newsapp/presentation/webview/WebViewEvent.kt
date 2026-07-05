package com.newsapp.presentation.webview

/**
 * UI Events for the WebView Screen.
 */
sealed interface WebViewEvent {
    data class OnUrlChanged(val url: String) : WebViewEvent
    data class OnProgressChanged(val progress: Int) : WebViewEvent
    data class OnTitleChanged(val title: String) : WebViewEvent
    data class OnLoadingChanged(val isLoading: Boolean) : WebViewEvent
    data class OnError(val message: String?) : WebViewEvent
    data object OnRetry : WebViewEvent
    data object OnShare : WebViewEvent
    data object OnOpenInBrowser : WebViewEvent
    data object OnBackPressed : WebViewEvent
}
