package com.newsapp.presentation.webview

import com.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor() : BaseViewModel<WebViewUiState, WebViewSideEffect>(WebViewUiState()) {

    fun onEvent(event: WebViewEvent) {
        when (event) {
            is WebViewEvent.OnUrlChanged -> updateState { it.copy(url = event.url) }
            is WebViewEvent.OnProgressChanged -> updateState { it.copy(progress = event.progress) }
            is WebViewEvent.OnTitleChanged -> updateState { it.copy(title = event.title) }
            is WebViewEvent.OnLoadingChanged -> updateState { it.copy(isLoading = event.isLoading) }
            is WebViewEvent.OnError -> updateState { it.copy(error = event.message) }
            WebViewEvent.OnRetry -> updateState { it.copy(error = null, progress = 0) }
            WebViewEvent.OnShare -> {
                sendEffect(WebViewSideEffect.ShareUrl(currentState.url, currentState.title))
            }
            WebViewEvent.OnOpenInBrowser -> {
                sendEffect(WebViewSideEffect.OpenInExternalBrowser(currentState.url))
            }
            WebViewEvent.OnBackPressed -> {
                sendEffect(WebViewSideEffect.NavigateBack)
            }
        }
    }
}
