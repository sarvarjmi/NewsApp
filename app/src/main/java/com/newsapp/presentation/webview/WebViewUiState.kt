package com.newsapp.presentation.webview

/**
 * UI State for the WebView Screen.
 *
 * @property url The current URL being loaded.
 * @property title The title of the page being loaded.
 * @property isLoading Indicates if the page is currently loading.
 * @property progress The current loading progress (0 to 100).
 * @property error An optional error message if loading fails.
 * @property isOffline Indicates if the device is currently offline.
 */
data class WebViewUiState(
    val url: String = "",
    val title: String = "News Source",
    val isLoading: Boolean = false,
    val progress: Int = 0,
    val error: String? = null,
    val isOffline: Boolean = false
)
