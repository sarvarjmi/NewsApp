package com.newsapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsapp.data.local.entity.NewsArticleEntity
import com.newsapp.data.local.model.NewsArticleWithBookmarkStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticles(articles: List<NewsArticleEntity>)

    @Query("SELECT * FROM news_articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesPagingSource(category: String): PagingSource<Int, NewsArticleEntity>

    @Query("""
        SELECT news_articles.*, (bookmarks.url IS NOT NULL) AS isBookmarked 
        FROM news_articles 
        LEFT JOIN bookmarks ON news_articles.url = bookmarks.url 
        WHERE category = :category 
        ORDER BY publishedAt DESC
    """)
    fun getArticlesWithBookmarkStatusPagingSource(category: String): PagingSource<Int, NewsArticleWithBookmarkStatus>

    @Query("SELECT * FROM news_articles WHERE url = :url LIMIT 1")
    suspend fun getArticleByUrl(url: String): NewsArticleEntity?

    @Query("DELETE FROM news_articles WHERE category = :category")
    suspend fun deleteArticlesByCategory(category: String)

    @Query("DELETE FROM news_articles WHERE cachedAt < :threshold")
    suspend fun deleteOldArticles(threshold: Long)

    @Query("DELETE FROM news_articles")
    suspend fun clearAll()
}
