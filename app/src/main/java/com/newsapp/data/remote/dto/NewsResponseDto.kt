package com.newsapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the root response from the News API.
 * This DTO maps the top-level structure of the JSON returned by endpoints like 
 * 'top-headlines' or 'everything'.
 * 
 * @property status If the request was successful or not. Options: ok, error.
 * @property totalResults The total number of results available for your request.
 * @property articles The list of articles matching the request.
 */
@Serializable
data class NewsResponseDto(
    @SerialName("status")
    val status: String,
    
    @SerialName("totalResults")
    val totalResults: Int,
    
    @SerialName("articles")
    val articles: List<ArticleDto>
)
