package com.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database Entity representing a bookmarked news article.
 * 
 * This model is optimized for SQLite storage via Room.
 */
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey 
    val url: String,
    val title: String,
    val author: String?,
    val description: String?,
    val content: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceId: String?,
    val sourceName: String
)
