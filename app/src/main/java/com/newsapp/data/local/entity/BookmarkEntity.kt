package com.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Database Entity representing a bookmarked news article.
 * 
 * @Entity: Marks this class as a Room entity, which corresponds to a table in SQLite.
 * @tableName: Explicitly defines the name of the table as "bookmarks".
 * @indices: Creates an index on 'publishedAt' and 'bookmarkedAt' for faster sorting of the news feed and bookmark list.
 */
@Entity(
    tableName = "bookmarks",
    indices = [
        Index(value = ["publishedAt"]),
        Index(value = ["bookmarkedAt"]),
        Index(value = ["title"]) // For faster search/lookup
    ]
)
data class BookmarkEntity(
    /**
     * @PrimaryKey: Marks 'url' as the unique identifier for each record.
     * Since every news article has a unique canonical URL, it serves as a natural key.
     */
    @PrimaryKey 
    val url: String,
    
    /**
     * The headline or title of the news article.
     */
    val title: String,
    
    /**
     * The author of the article. Nullable as some sources don't provide this.
     */
    val author: String?,
    
    /**
     * A short summary of the article content.
     */
    val description: String?,
    
    /**
     * The main text of the article. Stored for offline reading.
     */
    val content: String?,
    
    /**
     * The URL of the representative image for the article.
     */
    val urlToImage: String?,
    
    /**
     * The date and time the article was originally published (ISO 8601 string).
     */
    val publishedAt: String,
    
    /**
     * The timestamp (in milliseconds) when the user added this bookmark.
     * Used to sort the bookmark list by "Recently Added".
     */
    val bookmarkedAt: Long,
    
    /**
     * The identifier for the news source (e.g., "bbc-news").
     */
    val sourceId: String?,
    
    /**
     * The display name of the news source (e.g., "BBC News").
     */
    val sourceName: String
)
