package com.newsapp.domain.usecase

import androidx.paging.PagingData
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching paginated top headlines.
 */
class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Executes the use case to retrieve a Flow of PagingData.
     * 
     * @param category Optional category to filter headlines.
     * @return Flow containing the paginated article data.
     */
    operator fun invoke(category: String? = null): Flow<PagingData<Article>> {
        return repository.getTopHeadlines(category)
    }
}
