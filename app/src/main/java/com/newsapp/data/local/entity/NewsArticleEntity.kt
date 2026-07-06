package com.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database Entity representing a cached news article in the main feed.
 * 
 * Unlike bookmarks, these articles are temporary and managed by background cleanup workers.
 */
@Entity(tableName = "news_articles")
data class NewsArticleEntity(
    @PrimaryKey
    val url: String,
    val title: String,
    val author: String?,
    val description: String?,
    val content: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceId: String?,
    val sourceName: String,
    val category: String,
    val cachedAt: Long = System.currentTimeMillis()
)
