package com.newsapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.newsapp.presentation.common.CategoryChip
import com.newsapp.presentation.common.NewsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Business", "Technology", "Sports", "Health", "Science", "Entertainment")

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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(categories) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = selectedCategory == category,
                        onSelected = { selectedCategory = it }
                    )
                }
            }

            LazyColumn {
                item {
                    Text(
                        text = "Breaking News",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                items(10) {
                    NewsCard(
                        title = "Sample News Title $it - This is a very long title to test the ellipsis and layout of the news card component.",
                        imageUrl = "https://via.placeholder.com/150",
                        source = "BBC News",
                        date = "2 hours ago",
                        onClick = { onNavigateToDetail("https://example.com/article/$it") },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        description = "This is a sample description for the news article. It provides a brief summary of what the article is about."
                    )
                }
            }
        }
    }
}
