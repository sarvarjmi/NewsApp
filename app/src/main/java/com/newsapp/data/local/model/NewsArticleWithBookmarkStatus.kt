package com.newsapp.data.local.model

import androidx.room.Embedded
import com.newsapp.data.local.entity.NewsArticleEntity

/**
 * Model that combines a cached news article with its bookmark status.
 */
data class NewsArticleWithBookmarkStatus(
    @Embedded val article: NewsArticleEntity,
    val isBookmarked: Boolean
)
