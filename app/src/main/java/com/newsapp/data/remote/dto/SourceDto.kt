package com.newsapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing the source of a news article.
 * 
 * This class is used as a nested object within [ArticleDto] to represent 
 * where the news originates (e.g., The Verge, Wired).
 */
@Serializable
data class SourceDto(
    /**
     * The identifier for the news source.
     * 
     * This is often null for sources that are not explicitly tracked by the 
     * News API's internal system (e.g., small blogs). 
     * When present, it provides a stable ID like "google-news" or "bbc-news".
     */
    @SerialName("id")
    val id: String?,
    
    /**
     * The display name for the news source.
     * 
     * While typically provided, we mark it as nullable to handle edge cases where 
     * the API might return incomplete data, preventing the app from crashing 
     * during JSON parsing.
     */
    @SerialName("name")
    val name: String?
)
