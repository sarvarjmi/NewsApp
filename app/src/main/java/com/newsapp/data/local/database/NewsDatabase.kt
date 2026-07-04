package com.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.newsapp.data.local.dao.NewsDao
import com.newsapp.data.local.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract val newsDao: NewsDao
}
