package com.newsapp.domain.usecase.bookmark

import com.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * Use Case to check if a specific article is bookmarked.
 */
class IsBookmarkedUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Performs an efficient existence check in the local database.
     * 
     * @param url The unique identifier (canonical URL) of the article.
     * @return true if the article is saved, false otherwise.
     */
    suspend operator fun invoke(url: String): Boolean {
        return repository.isBookmarked(url)
    }
}
