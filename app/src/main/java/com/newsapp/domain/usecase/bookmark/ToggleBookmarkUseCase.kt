package com.newsapp.domain.usecase.bookmark

import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * Use Case to toggle the bookmark status of an article.
 * 
 * If the article is already bookmarked, it will be removed. 
 * If it is not bookmarked, it will be added to the database.
 */
class ToggleBookmarkUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Executes the toggle logic.
     * 
     * @param article The [Article] to save or remove.
     */
    suspend operator fun invoke(article: Article) {
        repository.toggleBookmark(article)
    }
}
