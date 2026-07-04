package com.newsapp.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.compose.collectAsLazyPagingItems
import com.newsapp.presentation.common.SearchBar
import com.newsapp.presentation.home.NewsPagingList
import com.newsapp.ui.theme.MaterialThemeSpacing

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = "Search",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(MaterialThemeSpacing.medium)
            )
            SearchBar(
                text = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                onSearch = { /* Handled reactively by flatMapLatest */ }
            )
            Spacer(modifier = Modifier.height(MaterialThemeSpacing.medium))
            
            NewsPagingList(
                articles = searchResults,
                onArticleClick = onNavigateToDetail
            )
        }
    }
}
