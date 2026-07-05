package com.newsapp.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.newsapp.ui.theme.NewsAppTheme

/**
 * A stateless animated bookmark toggle button.
 *
 * @param isBookmarked Whether the article is currently saved.
 * @param onClick Callback triggered when the button is clicked.
 * @param modifier Modifier for the button layout.
 * @param contentDescription Optional custom content description for accessibility.
 */
@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val description = contentDescription ?: if (isBookmarked) "Remove from bookmarks" else "Save to bookmarks"
    
    // Animation logic for scale and color
    val scale by animateFloatAsState(
        targetValue = if (isBookmarked) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "bookmarkScale"
    )
    
    val tint by animateColorAsState(
        targetValue = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "bookmarkTint"
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(48.dp) // Accessibility: Standard touch target size
            .semantics { this.contentDescription = description }
    ) {
        Icon(
            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
            contentDescription = null, // Handled by IconButton semantics
            modifier = Modifier
                .size(24.dp)
                .scale(scale),
            tint = tint
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkButtonPreview() {
    NewsAppTheme {
        BookmarkButton(isBookmarked = true, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkButtonUnselectedPreview() {
    NewsAppTheme {
        BookmarkButton(isBookmarked = false, onClick = {})
    }
}
