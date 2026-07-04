package com.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.newsapp.core.common.DatabaseConstants
import com.newsapp.data.local.dao.BookmarkDao
import com.newsapp.data.local.entity.BookmarkEntity

@Database(
    entities = [BookmarkEntity::class],
    version = DatabaseConstants.DATABASE_VERSION,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao
}
