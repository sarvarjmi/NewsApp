package com.newsapp.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.newsapp.domain.model.Article
import com.newsapp.ui.theme.MaterialThemeSpacing

/**
 * A reusable paginated list component for displaying news articles.
 *
 * Performance Optimizations:
 * 1. [itemKey]: Stable identities for articles (URL).
 * 2. [itemContentType]: Efficient composition reuse.
 * 3. [LazyListState]: Shared state for scroll control.
 */
@Composable
fun NewsPagingList(
    articles: LazyPagingItems<Article>,
    lazyListState: LazyListState,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    onRetry: () -> Unit,
    isOnline: Boolean = true,
    header: (@Composable () -> Unit)? = null
) {
    // Automatic reconnect: Trigger retry when coming back online if in error state
    LaunchedEffect(isOnline) {
        if (isOnline && (articles.loadState.refresh is LoadState.Error || articles.loadState.append is LoadState.Error)) {
            onRetry()
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .testTag("news_list"),
        contentPadding = PaddingValues(bottom = MaterialThemeSpacing.large)
    ) {
        if (header != null) {
            item(contentType = "Header") {
                header()
            }
        }

        // Main paginated items with Performance Optimizations
        items(
            count = articles.itemCount, 
            key = articles.itemKey { it.url },
            contentType = articles.itemContentType { "ArticleCard" }
        ) { index ->
            articles[index]?.let { article ->
                NewsCard(
                    title = article.title,
                    imageUrl = article.urlToImage ?: "",
                    source = article.source.name,
                    date = article.publishedAt,
                    isBookmarked = article.isBookmarked,
                    onBookmarkClick = { onBookmarkClick(article) },
                    onClick = { onArticleClick(article) },
                    modifier = Modifier.padding(
                        horizontal = MaterialThemeSpacing.medium, 
                        vertical = MaterialThemeSpacing.small
                    ),
                    description = article.description
                )
            }
        }

        // Handle Paging Load States
        item(contentType = "StatusState") {
            articles.apply {
                when {
                    // Initial Load: Show Shimmer
                    loadState.refresh is LoadState.Loading && itemCount == 0 -> {
                        repeat(6) { LoadingShimmer() }
                    }

                    // Load Error: Show ErrorView with Retry
                    loadState.refresh is LoadState.Error -> {
                        val error = loadState.refresh as LoadState.Error
                        ErrorView(
                            message = error.error.localizedMessage ?: "Failed to load news", 
                            onRetry = onRetry
                        )
                    }

                    // Success but empty: Show EmptyState
                    loadState.refresh is LoadState.NotLoading && itemCount == 0 -> {
                        EmptyState(message = "No articles found.", title = "Nothing here")
                    }

                    // Pagination Loading (Append)
                    loadState.append is LoadState.Loading -> {
                        Box(
                            Modifier.fillMaxWidth().padding(16.dp), 
                            contentAlignment = Alignment.Center
                        ) { 
                            CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp) 
                        }
                    }
                }
            }
        }
    }
}
