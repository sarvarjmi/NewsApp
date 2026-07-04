package com.newsapp.domain.usecase.bookmark

import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * Use Case to remove an article from the local bookmarks database.
 */
class RemoveBookmarkUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Deletes the article from local storage.
     * 
     * @param article The [Article] domain model to be removed.
     */
    suspend operator fun invoke(article: Article) {
        repository.removeBookmark(article)
    }
}
