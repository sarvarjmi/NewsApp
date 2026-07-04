package com.newsapp.data.remote.mapper

import com.newsapp.data.remote.dto.ArticleDto
import com.newsapp.domain.model.Article

fun ArticleDto.toDomain(): Article {
    return Article(
        title = title,
        description = description,
        content = content,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        sourceName = source.name
    )
}
