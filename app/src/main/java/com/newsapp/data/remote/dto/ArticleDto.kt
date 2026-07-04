package com.newsapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing a single news article from the API.
 * 
 * @property source The source of the article (id and name).
 * @property author The author of the article. Can be null.
 * @property title The headline or title of the article.
 * @property description A summary or description of the article. Can be null.
 * @property url The direct URL to the article on the news website.
 * @property urlToImage The URL to the representative image for the article. Can be null.
 * @property publishedAt The date and time the article was published in ISO 8601 format.
 * @property content The unformatted content of the article (often truncated). Can be null.
 */
@Serializable
data class ArticleDto(
    @SerialName("source")
    val source: SourceDto,
    
    @SerialName("author")
    val author: String?,
    
    @SerialName("title")
    val title: String,
    
    @SerialName("description")
    val description: String?,
    
    @SerialName("url")
    val url: String,
    
    @SerialName("urlToImage")
    val urlToImage: String?,
    
    @SerialName("publishedAt")
    val publishedAt: String,
    
    @SerialName("content")
    val content: String?
)
