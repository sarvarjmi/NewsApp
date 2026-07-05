package com.newsapp.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.newsapp.presentation.common.SearchBar
import com.newsapp.presentation.home.NewsPagingList
import com.newsapp.ui.theme.MaterialThemeSpacing

/**
 * The Search Screen implementation for NewsApp.
 * 
 * Provides a debounced, reactive search experience with Paging 3 integration.
 * It handles loading, empty results, success, and error states gracefully.
 *
 * @param viewModel The [SearchViewModel] that manages the search state and results.
 * @param onNavigateToDetail Callback to navigate to the article detail screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    // Collect UI state with lifecycle awareness
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // Collect paginated search results
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    
    val focusRequester = remember { FocusRequester() }
    val pullToRefreshState = rememberPullToRefreshState()
    
    // Determine if refreshing based on Paging load state
    val isRefreshing = searchResults.loadState.refresh is LoadState.Loading && searchResults.itemCount > 0

    // Handle side effects (navigation)
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchSideEffect.NavigateToDetail -> onNavigateToDetail(effect.url)
            }
        }
    }

    // Auto-focus the search bar when the screen is first entered
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Search",
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
                // Integrated SearchBar with state hoisting
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = { viewModel.onEvent(SearchEvent.OnQueryChanged(it)) },
                    onSearch = { 
                        // The ViewModel handles search reactively via debounce in the Flow,
                        // but we could explicitly trigger logic here if needed.
                    },
                    onClear = { viewModel.onEvent(SearchEvent.OnQueryChanged("")) },
                    focusRequester = focusRequester
                )
                
                Spacer(modifier = Modifier.height(MaterialThemeSpacing.medium))
                
                // Reusable Paginated List component from Home
                NewsPagingList(
                    articles = searchResults,
                    onArticleClick = { article -> 
                        viewModel.onEvent(SearchEvent.OnArticleClicked(article.url)) 
                    },
                    onBookmarkClick = { article ->
                        viewModel.onEvent(SearchEvent.OnBookmarkToggled(article))
                    },
                    onRetry = { searchResults.retry() }
                )
            }
        }
    }
}
