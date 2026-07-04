package com.newsapp.domain.usecase

import androidx.paging.PagingData
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for searching articles based on a query.
 */
class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Executes the search.
     * 
     * @param query The search term.
     * @return Flow containing the paginated search results.
     */
    operator fun invoke(query: String): Flow<PagingData<Article>> {
        return repository.searchNews(query)
    }
}
