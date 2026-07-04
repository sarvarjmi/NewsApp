package com.newsapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.newsapp.data.remote.api.NewsApiService
import com.newsapp.data.remote.mapper.toDomain
import com.newsapp.domain.model.Article
import timber.log.Timber

/**
 * A [PagingSource] implementation for fetching news articles from the [NewsApiService].
 * 
 * This class handles the pagination logic for both top headlines and search queries.
 * It transforms the network DTOs into Domain models using the provided mappers.
 * 
 * @property newsApiService The Retrofit service used to make API calls.
 * @property category Optional news category to filter top headlines.
 * @property query Optional search query for the search endpoint.
 */
class NewsPagingSource(
    private val newsApiService: NewsApiService,
    private val category: String? = null,
    private val query: String? = null
) : PagingSource<Int, Article>() {

    /**
     * The refresh key is used for subsequent refresh calls to PagingSource.load after the 
     * initial load.
     * 
     * We return the page key of the closest page to the anchor position to ensure that 
     * when the list is invalidated (e.g. on refresh), the user stays at the same scroll position.
     */
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    /**
     * Loads a page of data from the News API.
     * 
     * Design Decisions:
     * - Key Type: [Int] represents the page number (starting from 1).
     * - Value Type: [Article] is our Domain model, ensuring the UI layer doesn't depend on DTOs.
     * - Error Handling: Catches all exceptions and returns [LoadResult.Error] to be handled by the UI.
     * - Empty Responses: Returns [null] for nextKey if the articles list is empty, signaling end of pagination.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        // Start from page 1 if no key is provided
        val page = params.key ?: 1
        
        return try {
            Timber.d("PagingSource: Loading page $page for category=$category, query=$query")
            
            // Call either 'everything' or 'top-headlines' based on the presence of a query
            val response = if (query != null) {
                newsApiService.searchNews(
                    query = query,
                    page = page,
                    pageSize = params.loadSize
                )
            } else {
                newsApiService.getTopHeadlines(
                    category = category,
                    page = page,
                    pageSize = params.loadSize
                )
            }
            
            // Map DTO articles to Domain Article models
            val articles = response.articles.map { it.toDomain() }
            
            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                // If the response contains no articles, we've reached the end
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Timber.e(e, "PagingSource: Error loading page $page")
            LoadResult.Error(e)
        }
    }
}
