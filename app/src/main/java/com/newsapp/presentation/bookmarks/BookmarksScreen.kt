package com.newsapp.presentation.bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.newsapp.presentation.common.EmptyState

@Composable
fun BookmarksScreen(
    onNavigateToDetail: (String) -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = "Bookmarks",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            
            // Empty state placeholder
            EmptyState(
                message = "You haven't saved any articles yet. Start exploring and bookmark your favorite news!",
                title = "No Bookmarks"
            )
        }
    }
}
