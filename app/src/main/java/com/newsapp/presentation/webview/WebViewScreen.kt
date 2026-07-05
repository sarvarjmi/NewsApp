package com.newsapp.presentation.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.newsapp.presentation.common.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    url: String,
    onBackClick: () -> Unit,
    viewModel: WebViewViewModel = hiltViewModel(),
    webViewClientFactory: WebViewClientFactory = remember { WebViewClientFactory() }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(url) {
        viewModel.onEvent(WebViewEvent.OnUrlChanged(url))
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                WebViewSideEffect.NavigateBack -> onBackClick()
                is WebViewSideEffect.OpenInExternalBrowser -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(effect.url)))
                }
                is WebViewSideEffect.ShareUrl -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "${effect.title}\n${effect.url}")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Article"))
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(WebViewEvent.OnBackPressed) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(WebViewEvent.OnShare) }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { viewModel.onEvent(WebViewEvent.OnOpenInBrowser) }) {
                        Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = "Open in Browser")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                LinearProgressIndicator(
                    progress = { state.progress / 100f },
                    modifier = Modifier.fillMaxWidth().height(2.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                if (state.error != null) {
                    ErrorView(
                        message = state.error!!,
                        onRetry = { viewModel.onEvent(WebViewEvent.OnRetry) }
                    )
                } else {
                    WebViewContainer(
                        url = url,
                        onEvent = { viewModel.onEvent(it) },
                        webViewClientFactory = webViewClientFactory
                    )
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebViewContainer(
    url: String,
    onEvent: (WebViewEvent) -> Unit,
    webViewClientFactory: WebViewClientFactory
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                    
                    // Security: Disable file access
                    allowFileAccess = false
                    allowContentAccess = false
                    
                    // Safe Browsing
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        safeBrowsingEnabled = true
                    }
                    
                    // Mixed content disabled by default in newer Android, but explicitly set for clarity
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
                }
                
                webViewClient = webViewClientFactory.create(onEvent)
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        onEvent(WebViewEvent.OnProgressChanged(newProgress))
                    }
                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        title?.let { onEvent(WebViewEvent.OnTitleChanged(it)) }
                    }
                }
                loadUrl(url)
            }
        },
        update = { webView ->
            // Update logic if needed
        }
    )
}
