package com.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsapp.data.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: BookmarkEntity)

    @Delete
    suspend fun delete(article: BookmarkEntity)

    @Query("SELECT * FROM bookmarks")
    fun getArticles(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE url = :url")
    suspend fun getArticle(url: String): BookmarkEntity?
}
