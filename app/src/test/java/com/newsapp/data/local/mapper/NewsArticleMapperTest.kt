package com.newsapp.data.local.mapper

import com.newsapp.data.local.entity.NewsArticleEntity
import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class NewsArticleMapperTest {

    @Test
    fun `toDomain should correctly map NewsArticleEntity to Article`() {
        // Arrange
        val entity = NewsArticleEntity(
            url = "https://example.com",
            title = "Test Title",
            author = "Author",
            description = "Description",
            content = "Content",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2024-03-20T10:00:00Z",
            sourceId = "source-id",
            sourceName = "Source Name",
            category = "general",
            cachedAt = 123456789L
        )

        // Act
        val result = entity.toDomain()

        // Assert
        assertThat(result.url).isEqualTo(entity.url)
        assertThat(result.title).isEqualTo(entity.title)
        assertThat(result.author).isEqualTo(entity.author)
        assertThat(result.description).isEqualTo(entity.description)
        assertThat(result.content).isEqualTo(entity.content)
        assertThat(result.urlToImage).isEqualTo(entity.urlToImage)
        assertThat(result.publishedAt).isEqualTo(entity.publishedAt)
        assertThat(result.source.id).isEqualTo(entity.sourceId)
        assertThat(result.source.name).isEqualTo(entity.sourceName)
    }

    @Test
    fun `toCacheEntity should correctly map Article to NewsArticleEntity`() {
        // Arrange
        val article = Article(
            url = "https://example.com",
            title = "Test Title",
            author = "Author",
            description = "Description",
            content = "Content",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2024-03-20T10:00:00Z",
            source = Source(id = "source-id", name = "Source Name")
        )
        val category = "business"

        // Act
        val result = article.toCacheEntity(category)

        // Assert
        assertThat(result.url).isEqualTo(article.url)
        assertThat(result.title).isEqualTo(article.title)
        assertThat(result.author).isEqualTo(article.author)
        assertThat(result.description).isEqualTo(article.description)
        assertThat(result.content).isEqualTo(article.content)
        assertThat(result.urlToImage).isEqualTo(article.urlToImage)
        assertThat(result.publishedAt).isEqualTo(article.publishedAt)
        assertThat(result.sourceId).isEqualTo(article.source.id)
        assertThat(result.sourceName).isEqualTo(article.source.name)
        assertThat(result.category).isEqualTo(category)
    }
}
