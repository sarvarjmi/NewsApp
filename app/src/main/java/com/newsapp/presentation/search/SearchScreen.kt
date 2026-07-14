package com.newsapp.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.ui.res.stringResource
import com.newsapp.R
import com.newsapp.domain.model.Article
import com.newsapp.presentation.common.NewsPagingList
import com.newsapp.presentation.common.SearchBar
import com.newsapp.ui.theme.MaterialThemeSpacing

/**
 * The Search Screen implementation for NewsApp.
 *
 * Performance Optimizations:
 * 1. [rememberLazyListState]: Persists result scroll position.
 * 2. Shared Paging Logic: Uses [NewsPagingList] with stable keys and content types.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    isOnline: Boolean,
    onNavigateToDetail: (Article) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val pullToRefreshState = rememberPullToRefreshState()

    val isRefreshing = searchResults.loadState.refresh is LoadState.Loading && searchResults.itemCount > 0

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchSideEffect.NavigateToDetail -> onNavigateToDetail(effect.article)
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { searchResults.refresh() },
            state = pullToRefreshState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = { viewModel.onEvent(SearchEvent.OnQueryChanged(it)) },
                    onSearch = { },
                    onClear = { viewModel.onEvent(SearchEvent.OnQueryChanged("")) },
                    focusRequester = focusRequester
                )

                Spacer(modifier = Modifier.height(MaterialThemeSpacing.medium))

                NewsPagingList(
                    articles = searchResults,
                    lazyListState = lazyListState,
                    onArticleClick = { article ->
                        viewModel.onEvent(SearchEvent.OnArticleClicked(article))
                    },
                    onBookmarkClick = { article ->
                        viewModel.onEvent(SearchEvent.OnBookmarkToggled(article))
                    },
                    onRetry = { searchResults.retry() },
                    isOnline = isOnline,
                    enableInitialShimmer = !state.isInitialLoad
                )
            }
        }
    }
}
