package com.newsapp.domain.usecase.bookmark

import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use Case to observe the complete list of saved bookmarks.
 * 
 * Provides a reactive stream that updates whenever a bookmark is added or removed.
 */
class ObserveBookmarksUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Retrieves the bookmark stream.
     * 
     * @return A [Flow] of article lists, sorted by the date they were bookmarked.
     */
    operator fun invoke(): Flow<List<Article>> {
        return repository.observeBookmarks()
    }
}
