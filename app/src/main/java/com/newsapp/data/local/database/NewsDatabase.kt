package com.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.newsapp.core.common.DatabaseConstants
import com.newsapp.data.local.dao.BookmarkDao
import com.newsapp.data.local.dao.NewsArticleDao
import com.newsapp.data.local.dao.NewsRemoteKeysDao
import com.newsapp.data.local.entity.BookmarkEntity
import com.newsapp.data.local.entity.NewsArticleEntity
import com.newsapp.data.local.entity.NewsRemoteKeysEntity

@Database(
    entities = [
        BookmarkEntity::class,
        NewsArticleEntity::class,
        NewsRemoteKeysEntity::class
    ],
    version = DatabaseConstants.DATABASE_VERSION,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao
    abstract val newsArticleDao: NewsArticleDao
    abstract val newsRemoteKeysDao: NewsRemoteKeysDao
}
