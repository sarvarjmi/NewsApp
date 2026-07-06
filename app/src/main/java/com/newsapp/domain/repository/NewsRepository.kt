package com.newsapp.domain.repository

import androidx.paging.PagingData
import com.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for news-related data operations in the NewsApp.
 *
 * This repository follows the Repository Pattern, acting as an abstraction layer 
 * between the Domain and Data layers. It serves as the Single Source of Truth (SSOT), 
 * encapsulating the logic for fetching news from a remote API and persisting 
 * bookmarks in a local database.
 *
 * Key Architectural Decisions:
 * 1. **Domain Purity**: This interface is located in the Domain layer and uses only 
 *    Domain models ([Article]). It remains agnostic of implementation details like 
 *    Retrofit DTOs or Room Entities.
 * 2. **Reactive Streams**: Uses [Flow] to provide reactive data updates, especially 
 *    for observing the bookmark list and paginated news.
 * 3. **Paging Integration**: Returns [PagingData] to support efficient infinite 
 *    scrolling with the Paging 3 library.
 * 4. **Concurrency**: Write operations are marked as 'suspend' to ensure they are 
 *    executed within a coroutine context, typically off the main thread.
 */
interface NewsRepository {

    /**
     * Fetches a paginated stream of top headlines from the remote news source.
     *
     * @param category The news category to filter (e.g., business, technology).
     *                 Defaults to null for general headlines.
     * @return A [Flow] emitting [PagingData] containing [Article] domain models.
     */
    fun getTopHeadlines(category: String? = null): Flow<PagingData<Article>>

    /**
     * Searches for news articles globally based on a search query.
     *
     * @param query The search keywords or phrases provided by the user.
     * @return A [Flow] emitting [PagingData] containing [Article] domain models.
     */
    fun searchNews(query: String): Flow<PagingData<Article>>

    /**
     * Manually triggers a refresh of the remote news data.
     *
     * Although Paging 3 handles refreshes automatically via the UI layer, 
     * this method allows for explicit cache invalidation or forced synchronization.
     */
    suspend fun refreshNews()

    /**
     * Persists a news article into the local database as a bookmark.
     *
     * @param article The [Article] to be saved for offline reading.
     */
    suspend fun saveBookmark(article: Article)

    /**
     * Removes a previously saved news article from the local database.
     *
     * @param article The [Article] to be deleted from bookmarks.
     */
    suspend fun removeBookmark(article: Article)

    /**
     * Toggles the bookmark status of an article.
     * If the article exists locally, it will be deleted; otherwise, it will be saved.
     *
     * @param article The [Article] to toggle.
     */
    suspend fun toggleBookmark(article: Article)

    /**
     * Provides a reactive stream of all articles saved as bookmarks.
     *
     * @return A [Flow] that emits a new list of [Article]s whenever the 
     *         database content changes.
     */
    fun observeBookmarks(): Flow<List<Article>>

    /**
     * Checks if an article is currently saved in the local bookmarks.
     *
     * @param url The unique canonical URL of the article.
     * @return true if the article is bookmarked, false otherwise.
     */
    suspend fun isBookmarked(url: String): Boolean

    /**
     * Attempts to retrieve a news article from the local cache.
     * 
     * @param url The unique canonical URL of the article.
     * @return The [Article] if found in cache or bookmarks, null otherwise.
     */
    suspend fun getArticleFromCache(url: String): Article?
}
