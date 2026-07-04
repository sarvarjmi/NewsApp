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
        source = Source(
            id = sourceId,
            name = sourceName
        ),
        isBookmarked = true
    )
}

/**
 * Maps [Article] from the domain layer to [BookmarkEntity] for local persistence.
 * 
 * Mapping decisions:
 * - [url]: Maintained as the [PrimaryKey] to ensure unique entries and efficient lookups.
 * - [title]: Mandatory field persisted directly.
 * - [author], [description], [content], [urlToImage]: Nullable fields preserved to maintain data integrity.
 * - [publishedAt]: ISO 8601 string persisted for chronological sorting.
 * - [source]: Flattened into [sourceId] and [sourceName] as Room does not store complex objects natively without TypeConverters.
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
        sourceId = source.id,
        sourceName = source.name
    )
}
