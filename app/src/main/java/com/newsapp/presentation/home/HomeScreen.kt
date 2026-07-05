package com.newsapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.newsapp.ui.theme.MaterialThemeSpacing

/**
 * The main Home Screen integration.
 *
 * This Composable connects the UI to the [HomeViewModel], handles reactive state 
 * updates, collects paging data, and responds to one-time navigation side effects.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    // 1. Observe StateFlow: Collect UI metadata
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // 2. Collect Paging Data: Reactive stream of news articles
    val articles = viewModel.articles.collectAsLazyPagingItems()

    // 3. Handle Navigation Side Effects
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeSideEffect.NavigateToDetail -> onNavigateToDetail(effect.url)
                HomeSideEffect.NavigateToSearch -> onNavigateToSearch()
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()
    
    // Refresh state derived from Paging
    val isRefreshing = articles.loadState.refresh is LoadState.Loading && articles.itemCount > 0

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { 
                    Text(
                        text = "NewsApp", 
                        style = MaterialTheme.typography.headlineMedium, 
                        fontWeight = FontWeight.Bold
                    ) 
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(HomeEvent.OnSearchClicked) }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search News")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        // 4. Pull-to-refresh integration
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { 
                viewModel.onEvent(HomeEvent.OnRefresh)
                articles.refresh() 
            },
            state = pullToRefreshState,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                HomeCategoryRow(
                    categories = state.categories,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { viewModel.onEvent(HomeEvent.OnCategorySelected(it)) }
                )

                NewsPagingList(
                    articles = articles,
                    onArticleClick = { article -> 
                        viewModel.onEvent(HomeEvent.OnArticleClicked(article)) 
                    },
                    onBookmarkClick = { article -> 
                        viewModel.onEvent(HomeEvent.OnBookmarkToggled(article)) 
                    },
                    onRetry = {
                        viewModel.onEvent(HomeEvent.OnRetry)
                        articles.retry()
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeCategoryRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(bottom = MaterialThemeSpacing.small),
        contentPadding = PaddingValues(horizontal = MaterialThemeSpacing.medium)
    ) {
        itemsIndexed(categories) { index, category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategory == category,
                onSelected = onCategorySelected,
                modifier = Modifier.padding(
                    end = if (index < categories.lastIndex) MaterialThemeSpacing.small else 0.dp
                )
            )
        }
    }
}

@Composable
fun NewsPagingList(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    onRetry: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = MaterialThemeSpacing.large)
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
            key = articles.itemKey { it.url }
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

        // Load State Management
        item {
            articles.apply {
                when {
                    loadState.refresh is LoadState.Loading && itemCount == 0 -> {
                        repeat(6) { LoadingShimmer() }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = loadState.refresh as LoadState.Error
                        ErrorView(
                            message = error.error.localizedMessage ?: "Network error", 
                            onRetry = onRetry
                        )
                    }

                    loadState.refresh is LoadState.NotLoading && itemCount == 0 -> {
                        EmptyState(message = "No articles found.", title = "Nothing here")
                    }

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
