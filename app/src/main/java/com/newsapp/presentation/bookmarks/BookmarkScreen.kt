package com.newsapp.presentation.bookmarks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source
import com.newsapp.presentation.common.EmptyState
import com.newsapp.presentation.common.ErrorView
import com.newsapp.presentation.common.LoadingShimmer
import com.newsapp.presentation.common.NewsCard
import com.newsapp.ui.theme.MaterialThemeSpacing
import com.newsapp.ui.theme.NewsAppTheme

/**
 * The Bookmark Screen implementation for NewsApp.
 * 
 * It manages the offline article list, handling interactions like removal with 
 * undo support, delete confirmation dialogs, and pull-to-refresh.
 *
 * @param onNavigateToDetail Navigation callback to the article detail screen.
 * @param viewModel The [BookmarkViewModel] injected via Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    onNavigateToDetail: (Article) -> Unit,
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()

    // Side Effect handling for navigation and Snackbar feedback
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BookmarkSideEffect.NavigateToDetail -> onNavigateToDetail(effect.article)
                is BookmarkSideEffect.ShowUndoSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Removed from bookmarks",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(BookmarkEvent.OnUndoRemove(effect.article))
                    }
                }
            }
        }
    }

    // Optional Delete Confirmation Dialog
    if (state.articleToDelete != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(BookmarkEvent.OnDismissDeleteDialog) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(BookmarkEvent.OnConfirmDelete) }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(BookmarkEvent.OnDismissDeleteDialog) }) {
                    Text("Cancel")
                }
            },
            title = { Text("Remove Bookmark?") },
            text = { Text("Are you sure you want to remove this article from your bookmarks?") },
            shape = MaterialTheme.shapes.medium,
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MediumTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Bookmarks",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (state.bookmarkCount > 0) {
                            Text(
                                text = "${state.bookmarkCount} saved articles",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.onEvent(BookmarkEvent.OnRefresh) },
            state = pullToRefreshState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        BookmarkLoadingState()
                    }
                    state.error != null -> {
                        ErrorView(
                            message = state.error!!,
                            onRetry = { viewModel.onEvent(BookmarkEvent.OnRetryClicked) }
                        )
                    }
                    state.isEmpty -> {
                        EmptyState(
                            message = "Your reading list is empty. Bookmark articles to read them offline later.",
                            title = "No Bookmarks"
                        )
                    }
                    else -> {
                        BookmarkList(
                            articles = state.articles,
                            onArticleClick = { viewModel.onEvent(BookmarkEvent.OnArticleClicked(it)) },
                            onRemoveClick = { viewModel.onEvent(BookmarkEvent.OnDeleteIntent(it)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkList(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    onRemoveClick: (Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = MaterialThemeSpacing.large)
    ) {
        items(
            items = articles,
            key = { it.url }
        ) { article ->
            NewsCard(
                title = article.title,
                imageUrl = article.urlToImage ?: "",
                source = article.source.name,
                date = article.publishedAt,
                isBookmarked = true,
                onBookmarkClick = { onRemoveClick(article) },
                onClick = { onArticleClick(article) },
                modifier = Modifier.padding(
                    horizontal = MaterialThemeSpacing.medium,
                    vertical = MaterialThemeSpacing.small
                ),
                description = article.description
            )
        }
    }
}

@Composable
private fun BookmarkLoadingState() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(5) {
            LoadingShimmer()
        }
    }
}

/**
 * --- Previews ---
 */

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BookmarkScreenPreview() {
    val sampleArticle = Article(
        title = "The Future of AI in Mobile Development",
        author = "Developer",
        description = "Exploring generative AI.",
        content = "Long content...",
        url = "https://example.com/1",
        urlToImage = "",
        publishedAt = "1 day ago",
        source = Source(id = "tech", name = "TechCrunch"),
        isBookmarked = true
    )

    NewsAppTheme {
        Surface {
            Scaffold(
                topBar = {
                    MediumTopAppBar(title = { Text("Bookmarks") })
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    BookmarkList(
                        articles = listOf(sampleArticle),
                        onArticleClick = {},
                        onRemoveClick = {}
                    )
                }
            }
        }
    }
}
