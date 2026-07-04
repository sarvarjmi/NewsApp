package com.newsapp.domain.model

data class Article(
    val title: String,
    val description: String?,
    val content: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceName: String,
    val isBookmarked: Boolean = false
)
