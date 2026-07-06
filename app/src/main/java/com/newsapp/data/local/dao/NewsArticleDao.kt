package com.newsapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsapp.data.local.entity.NewsArticleEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing the temporary news articles cache.
 */
@Dao
interface NewsArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticles(articles: List<NewsArticleEntity>)

    @Query("SELECT * FROM news_articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesPagingSource(category: String): PagingSource<Int, NewsArticleEntity>

    @Query("DELETE FROM news_articles WHERE category = :category")
    suspend fun deleteArticlesByCategory(category: String)

    @Query("DELETE FROM news_articles WHERE cachedAt < :threshold")
    suspend fun deleteOldArticles(threshold: Long)

    @Query("DELETE FROM news_articles")
    suspend fun clearAll()
}
