package com.newsapp.domain.usecase.bookmark

import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * Use Case to save an article to the local bookmarks database.
 */
class AddBookmarkUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Persists the article for offline reading.
     * 
     * @param article The [Article] domain model to be saved.
     */
    suspend operator fun invoke(article: Article) {
        repository.saveBookmark(article)
    }
}
