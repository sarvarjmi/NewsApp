package com.newsapp.util

import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source
import com.newsapp.data.remote.dto.ArticleDto
import com.newsapp.data.remote.dto.SourceDto
import com.newsapp.data.local.entity.BookmarkEntity
import com.newsapp.data.local.entity.NewsArticleEntity

object TestData {

    val sampleSource = Source(id = "id", name = "Name")
    
    val sampleArticle = Article(
        url = "https://example.com",
        title = "Title",
        author = "Author",
        description = "Desc",
        content = "Content",
        urlToImage = "https://example.com/image.jpg",
        publishedAt = "2024-03-20T10:00:00Z",
        source = sampleSource,
        isBookmarked = false
    )

    val sampleArticleDto = ArticleDto(
        url = sampleArticle.url,
        title = sampleArticle.title,
        author = sampleArticle.author,
        description = sampleArticle.description,
        content = sampleArticle.content,
        urlToImage = sampleArticle.urlToImage,
        publishedAt = sampleArticle.publishedAt,
        source = SourceDto(id = sampleSource.id, name = sampleSource.name)
    )

    val sampleBookmarkEntity = BookmarkEntity(
        url = sampleArticle.url,
        title = sampleArticle.title,
        author = sampleArticle.author,
        description = sampleArticle.description,
        content = sampleArticle.content,
        urlToImage = sampleArticle.urlToImage,
        publishedAt = sampleArticle.publishedAt,
        sourceId = sampleSource.id,
        sourceName = sampleSource.name,
        bookmarkedAt = 123456789L
    )

    val sampleNewsArticleEntity = NewsArticleEntity(
        url = sampleArticle.url,
        title = sampleArticle.title,
        author = sampleArticle.author,
        description = sampleArticle.description,
        content = sampleArticle.content,
        urlToImage = sampleArticle.urlToImage,
        publishedAt = sampleArticle.publishedAt,
        sourceId = sampleSource.id,
        sourceName = sampleSource.name,
        category = "general",
        cachedAt = 123456789L
    )
}
