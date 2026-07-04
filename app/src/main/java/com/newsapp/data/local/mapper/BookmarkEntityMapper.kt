package com.newsapp.data.local.mapper

import com.newsapp.data.local.entity.BookmarkEntity
import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source

/**
 * Maps [BookmarkEntity] from the local database to [Article] in the domain layer.
 */
fun BookmarkEntity.toDomain(): Article {
    return Article(
        url = url,
        title = title,
        author = author,
        description = description,
        content = content,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        bookmarkedAt = bookmarkedAt,
        source = Source(
            id = sourceId,
            name = sourceName
        ),
        isBookmarked = true
    )
}

/**
 * Maps [Article] from the domain layer to [BookmarkEntity] for local persistence.
 */
fun Article.toEntity(): BookmarkEntity {
    return BookmarkEntity(
        url = url,
        title = title,
        author = author,
        description = description,
        content = content,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        bookmarkedAt = bookmarkedAt ?: System.currentTimeMillis(),
        sourceId = source.id,
        sourceName = source.name
    )
}
