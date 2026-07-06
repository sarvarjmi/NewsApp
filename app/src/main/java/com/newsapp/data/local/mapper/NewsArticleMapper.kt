package com.newsapp.data.local.mapper

import com.newsapp.data.local.entity.NewsArticleEntity
import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source

fun Article.toCacheEntity(category: String): NewsArticleEntity {
    return NewsArticleEntity(
        url = url,
        title = title,
        author = author,
        description = description,
        content = content,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        sourceId = source.id,
        sourceName = source.name,
        category = category
    )
}

fun NewsArticleEntity.toDomain(): Article {
    return Article(
        title = title,
        author = author,
        description = description,
        content = content,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        source = Source(id = sourceId, name = sourceName)
    )
}
