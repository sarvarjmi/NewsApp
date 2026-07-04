package com.newsapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.newsapp.domain.model.Article
import com.newsapp.presentation.common.CategoryChip
import com.newsapp.presentation.common.EmptyState
import com.newsapp.presentation.common.ErrorView
import com.newsapp.presentation.common.LoadingShimmer
import com.newsapp.presentation.common.NewsCard
import com.newsapp.presentation.common.PrimaryButton
import com.newsapp.ui.theme.MaterialThemeSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val articles = viewModel.articles.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NewsApp",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                },
                windowInsets = TopAppBarDefaults.windowInsets
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Category Selection Row
            LazyRow(
                modifier = Modifier.padding(bottom = MaterialThemeSpacing.small)
            ) {
                itemsIndexed(state.categories) { index, category ->
                    CategoryChip(
                        modifier = if (index == 0) Modifier.padding(start = MaterialThemeSpacing.medium) else Modifier,
                        category = category,
                        isSelected = state.selectedCategory == category,
                        onSelected = { viewModel.onEvent(HomeEvent.OnCategorySelected(it)) }
                    )
                }
            }

            // Paginated News List
            NewsPagingList(
                articles = articles,
                onArticleClick = onNavigateToDetail,
                onBookmarkClick = { article ->
                    viewModel.onEvent(HomeEvent.OnBookmarkToggled(article))
                }
            )
        }
    }
}

@Composable
fun NewsPagingList(
    articles: LazyPagingItems<Article>,
    onArticleClick: (String) -> Unit,
    onBookmarkClick: (Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = MaterialThemeSpacing.medium)
    ) {
        item {
            Text(
                text = "Breaking News",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    horizontal = MaterialThemeSpacing.medium,
                    vertical = MaterialThemeSpacing.small
                ),
                fontWeight = FontWeight.Bold
            )
        }

        items(
            count = articles.itemCount,
            key = articles.itemKey { it.url } // Optimization: Stable keys prevent redundant recompositions
        ) { index ->
            articles[index]?.let { article ->
                NewsCard(
                    title = article.title,
                    imageUrl = article.urlToImage ?: "",
                    source = article.source.name,
                    date = article.publishedAt,
                    isBookmarked = article.isBookmarked,
                    onBookmarkClick = { onBookmarkClick(article) },
                    onClick = { onArticleClick(article.url) },
                    modifier = Modifier.padding(
                        horizontal = MaterialThemeSpacing.medium,
                        vertical = MaterialThemeSpacing.small
                    ),
                    description = article.description
                )
            }
        }

        // Handle different LoadStates
        articles.apply {
            when {
                // Initial Load - Show Shimmer Skeletons
                loadState.refresh is LoadState.Loading -> {
                    item {
                        repeat(5) {
                            LoadingShimmer()
                        }
                    }
                }

                // Initial Load Error - Show Full Screen Error
                loadState.refresh is LoadState.Error -> {
                    val error = loadState.refresh as LoadState.Error
                    item {
                        ErrorView(
                            message = error.error.localizedMessage ?: "Unknown Error",
                            onRetry = { retry() }
                        )
                    }
                }

                // Empty State
                loadState.refresh is LoadState.NotLoading && articles.itemCount == 0 -> {
                    item {
                        EmptyState(
                            message = "No articles found for this category.",
                            title = "No News"
                        )
                    }
                }

                // Pagination Loading (Append) - Show Spinner at bottom
                loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialThemeSpacing.medium),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.wrapContentSize(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Pagination Error (Append) - Show Error with Retry at bottom
                loadState.append is LoadState.Error -> {
                    val error = loadState.append as LoadState.Error
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialThemeSpacing.medium),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = error.error.localizedMessage ?: "Failed to load more",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            PrimaryButton(
                                text = "Retry",
                                onClick = { retry() },
                                modifier = Modifier.padding(top = MaterialThemeSpacing.small)
                            )
                        }
                    }
                }
            }
        }
    }
}
