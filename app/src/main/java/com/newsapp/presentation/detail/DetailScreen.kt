package com.newsapp.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source
import com.newsapp.presentation.common.BookmarkButton
import com.newsapp.presentation.common.ErrorView
import com.newsapp.presentation.common.PrimaryButton
import com.newsapp.ui.theme.MaterialThemeSpacing
import com.newsapp.ui.theme.NewsAppTheme

/**
 * The Article Detail Screen.
 *
 * This screen provides an immersive reading experience for a news article.
 * It features a large hero image, full content view, and quick actions for 
 * sharing and bookmarking.
 *
 * @param articleUrl The unique identifier (URL) of the article.
 * @param onBackClick Callback for navigating back to the previous screen.
 * @param viewModel The Hilt-injected [DetailViewModel].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    articleUrl: String,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle One-time Side Effects (Navigation, External Apps)
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                DetailSideEffect.NavigateBack -> onBackClick()
                is DetailSideEffect.OpenBrowser -> {
                    try {
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        customTabsIntent.launchUrl(context, Uri.parse(effect.url))
                    } catch (e: Exception) {
                        // Fallback to system browser
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                        context.startActivity(intent)
                    }
                }
                is DetailSideEffect.ShareArticle -> {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, effect.title)
                        putExtra(Intent.EXTRA_TEXT, "${effect.title}\n\n${effect.url}")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Article"))
                }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(DetailEvent.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                actions = {
                    state.article?.let { article ->
                        IconButton(onClick = { viewModel.onEvent(DetailEvent.OnShareClicked(article)) }) {
                            Icon(
                                imageVector = Icons.Default.Share, 
                                contentDescription = "Share Article"
                            )
                        }
                        BookmarkButton(
                            isBookmarked = state.isBookmarked,
                            onClick = { viewModel.onEvent(DetailEvent.OnBookmarkToggled(article)) }
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.article != null -> {
                    DetailContent(
                        article = state.article!!,
                        readingTime = state.readingTime,
                        onOpenInBrowser = { 
                            viewModel.onEvent(DetailEvent.OnOpenInBrowserClicked(it)) 
                        }
                    )
                }
                state.error != null -> {
                    ErrorView(
                        message = state.error!!,
                        onRetry = { viewModel.onEvent(DetailEvent.OnRetryClicked) }
                    )
                }
            }
        }
    }
}

/**
 * The scrollable article content.
 */
@Composable
private fun DetailContent(
    article: Article,
    readingTime: String?,
    onOpenInBrowser: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Image
        AsyncImage(
            model = article.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(MaterialThemeSpacing.medium)
        ) {
            // Headline
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(MaterialThemeSpacing.small))

            // Metadata Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.source.name,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " • ${article.publishedAt}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                readingTime?.let {
                    Text(
                        text = " • $it",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialThemeSpacing.medium))

            // Article Body
            Text(
                text = article.content ?: article.description ?: "Full content is not available for this article preview.",
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(MaterialThemeSpacing.extraLarge))

            // Primary Action
            PrimaryButton(
                text = "Read Full Article",
                onClick = { onOpenInBrowser(article.url) }
            )
            
            Spacer(modifier = Modifier.height(MaterialThemeSpacing.large))
        }
    }
}

/**
 * --- Previews ---
 */

@Preview(showBackground = true)
@Composable
private fun DetailScreenContentPreview() {
    val sampleArticle = Article(
        title = "NASA’s James Webb Telescope Reveals New Insights into the Early Universe",
        author = "Jane Doe",
        description = "A detailed look at the latest findings from the world’s most powerful space telescope.",
        content = "The James Webb Space Telescope has once again captured the imagination of astronomers worldwide... [Truncated content to simulate real API data]",
        url = "https://example.com",
        urlToImage = "https://via.placeholder.com/600x400",
        publishedAt = "2 hours ago",
        source = Source(id = "nasa", name = "NASA News")
    )
    
    NewsAppTheme {
        Surface {
            DetailContent(
                article = sampleArticle,
                readingTime = "4 min read",
                onOpenInBrowser = {}
            )
        }
    }
}
