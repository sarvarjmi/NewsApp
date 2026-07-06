package com.newsapp.domain.usecase.news

import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * Use Case to retrieve a specific news article from the local cache.
 * 
 * This is useful for resolving deep links where only the URL is provided,
 * as it allows the app to show the "Native" detail screen if the data 
 * is already available locally.
 */
class GetArticleFromCacheUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(url: String): Article? {
        return repository.getArticleFromCache(url)
    }
}
