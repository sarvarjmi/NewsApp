package com.newsapp.domain.usecase.search

import androidx.paging.PagingData
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Use Case for searching news articles globally.
 * 
 * This class encapsulates the business logic for article search, including 
 * validation of the search query and delegation to the repository for 
 * paginated data retrieval.
 */
class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Executes the search operation.
     * 
     * @param query The search string provided by the user.
     * @return A [Flow] of [PagingData] containing the search results. 
     *         Returns an empty [PagingData] stream if the query is blank.
     */
    operator fun invoke(query: String): Flow<PagingData<Article>> {
        // Business Rule: Search query must not be blank to avoid unnecessary API calls
        if (query.isBlank()) {
            return flowOf(PagingData.empty())
        }
        
        return repository.searchNews(query)
    }
}
