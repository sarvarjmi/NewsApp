package com.newsapp.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.newsapp.presentation.common.CategoryChip
import com.newsapp.presentation.common.NewsPagingList
import com.newsapp.ui.theme.MaterialThemeSpacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    isOnline: Boolean,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val articles = viewModel.articles.collectAsLazyPagingItems()
    
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTop by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

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
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to top")
                }
            }
        }
    ) { innerPadding ->
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
                    lazyListState = lazyListState,
                    onArticleClick = { article -> 
                        viewModel.onEvent(HomeEvent.OnArticleClicked(article)) 
                    },
                    onBookmarkClick = { article -> 
                        viewModel.onEvent(HomeEvent.OnBookmarkToggled(article)) 
                    },
                    onRetry = {
                        viewModel.onEvent(HomeEvent.OnRetry)
                        articles.retry()
                    },
                    isOnline = isOnline,
                    header = {
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
        itemsIndexed(
            items = categories,
            key = { _, category -> category }
        ) { index, category ->
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
