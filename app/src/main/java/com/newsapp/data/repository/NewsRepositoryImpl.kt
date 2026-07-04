package com.newsapp.data.repository

import com.newsapp.data.local.dao.NewsDao
import com.newsapp.data.remote.api.NewsApiService
import com.newsapp.domain.model.Article
import com.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    override fun getTopHeadlines(category: String): Flow<List<Article>> = flow {
        // Placeholder
        emit(emptyList())
    }

    override fun searchNews(query: String): Flow<List<Article>> = flow {
        // Placeholder
        emit(emptyList())
    }

    override suspend fun upsertArticle(article: Article) {
        // Placeholder
    }

    override suspend fun deleteArticle(article: Article) {
        // Placeholder
    }

    override fun getBookmarkedArticles(): Flow<List<Article>> = flow {
        // Placeholder
        emit(emptyList())
    }

    override suspend fun getArticleByUrl(url: String): Article? {
        // Placeholder
        return null
    }
}
