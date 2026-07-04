package com.newsapp.data.remote.mapper

import com.newsapp.data.remote.dto.ArticleDto
import com.newsapp.domain.model.Article

/**
 * Maps [ArticleDto] from the data layer to [Article] in the domain layer.
 * 
 * Mapping decisions:
 * - [title]: Mandatory field mapped directly.
 * - [author]: Preserved as nullable string.
 * - [description]: Preserved as nullable string.
 * - [content]: Preserved as nullable string.
 * - [url]: Mandatory field mapped directly.
 * - [urlToImage]: Preserved as nullable string.
 * - [publishedAt]: Mandatory field mapped directly.
 * - [source]: Delegates mapping to [SourceDto.toDomain].
 * - [isBookmarked]: Defaults to false for new incoming articles.
 */
fun ArticleDto.toDomain(): Article {
    return Article(
        title = title,
        author = author,
        description = description,
        content = content,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        source = source.toDomain(),
        isBookmarked = false
    )
}
