package com.newsapp.domain.usecase.bookmark

import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use Case to observe the bookmark status of a specific article.
 */
class ObserveBookmarkStatusUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Observes the bookmark status of an article by its URL.
     * 
     * @param url The unique canonical URL of the article.
     * @return A [Flow] emitting true if the article is bookmarked, false otherwise.
     */
    operator fun invoke(url: String): Flow<Boolean> {
        return repository.observeBookmarks().map { bookmarks ->
            bookmarks.any { it.url == url }
        }
    }
}
