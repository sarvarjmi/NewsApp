package com.newsapp.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a news article.
 * 
 * This model is pure Kotlin and independent of any data-layer specific 
 * implementations like DTOs or Entities.
 */
@Serializable
data class Article(
    val title: String,
    val author: String?,
    val description: String?,
    val content: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val source: Source,
    val isBookmarked: Boolean = false,
    val bookmarkedAt: Long? = null
)
