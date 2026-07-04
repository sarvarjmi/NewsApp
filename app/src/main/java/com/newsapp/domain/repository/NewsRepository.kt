package com.newsapp.domain.repository

import com.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(category: String): Flow<List<Article>>
    fun searchNews(query: String): Flow<List<Article>>
    suspend fun upsertArticle(article: Article)
    suspend fun deleteArticle(article: Article)
    fun getBookmarkedArticles(): Flow<List<Article>>
    suspend fun getArticleByUrl(url: String): Article?
}
