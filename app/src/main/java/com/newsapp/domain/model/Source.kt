package com.newsapp.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing the source of a news article.
 * 
 * @property id The identifier for the news source (e.g., "cnn").
 * @property name The display name for the news source (e.g., "CNN").
 */
@Serializable
data class Source(
    val id: String?,
    val name: String
)
