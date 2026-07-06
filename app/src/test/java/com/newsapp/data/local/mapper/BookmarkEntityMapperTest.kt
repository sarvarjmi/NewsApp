package com.newsapp.data.local.mapper

import com.newsapp.data.local.entity.BookmarkEntity
import com.newsapp.domain.model.Article
import com.newsapp.domain.model.Source
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class BookmarkEntityMapperTest {

    @Test
    fun `toDomain should correctly map BookmarkEntity to Article`() {
        // Arrange
        val entity = BookmarkEntity(
            url = "https://example.com",
            title = "Test Title",
            author = "Author",
            description = "Description",
            content = "Content",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2024-03-20T10:00:00Z",
            sourceId = "source-id",
            sourceName = "Source Name",
            bookmarkedAt = 123456789L
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
        assertThat(result.bookmarkedAt).isEqualTo(entity.bookmarkedAt)
        assertThat(result.isBookmarked).isTrue()
    }

    @Test
    fun `toEntity should correctly map Article to BookmarkEntity`() {
        // Arrange
        val article = Article(
            url = "https://example.com",
            title = "Test Title",
            author = "Author",
            description = "Description",
            content = "Content",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2024-03-20T10:00:00Z",
            source = Source(id = "source-id", name = "Source Name"),
            bookmarkedAt = 987654321L
        )

        // Act
        val result = article.toEntity()

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
        assertThat(result.bookmarkedAt).isEqualTo(article.bookmarkedAt)
    }
}
