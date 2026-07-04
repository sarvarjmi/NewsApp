package com.newsapp.domain.repository

import androidx.paging.PagingData
import com.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Domain-level repository interface for News operations.
 */
interface NewsRepository {
    /**
     * Fetches paginated top headlines from the remote source.
     */
    fun getTopHeadlines(category: String?): Flow<PagingData<Article>>

    /**
     * Searches for articles using the provided query.
     */
    fun searchNews(query: String): Flow<PagingData<Article>>

    /**
     * Saves or updates an article in the local bookmark database.
     */
    suspend fun upsertArticle(article: Article)

    /**
     * Removes an article from the local bookmark database.
     */
    suspend fun deleteArticle(article: Article)

    /**
     * Observes the list of bookmarked articles from the local database.
     */
    fun getBookmarkedArticles(): Flow<List<Article>>

    /**
     * Checks if an article exists in bookmarks by its URL.
     */
    suspend fun getArticleByUrl(url: String): Article?
}
