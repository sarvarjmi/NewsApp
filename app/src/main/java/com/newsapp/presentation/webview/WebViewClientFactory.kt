package com.newsapp.presentation.webview

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebViewClientFactory @Inject constructor() {

    fun create(onEvent: (WebViewEvent) -> Unit): WebViewClient {
        return object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onEvent(WebViewEvent.OnLoadingChanged(true))
                url?.let { onEvent(WebViewEvent.OnUrlChanged(it)) }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onEvent(WebViewEvent.OnLoadingChanged(false))
                view?.title?.let { onEvent(WebViewEvent.OnTitleChanged(it)) }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                    onEvent(WebViewEvent.OnError(error?.description?.toString() ?: "Failed to load page"))
                }
            }
        }
    }
}
