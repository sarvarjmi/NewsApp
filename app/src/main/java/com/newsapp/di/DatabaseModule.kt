package com.newsapp.di

import android.content.Context
import androidx.room.Room
import com.newsapp.core.common.DatabaseConstants
import com.newsapp.data.local.dao.BookmarkDao
import com.newsapp.data.local.dao.NewsArticleDao
import com.newsapp.data.local.dao.NewsRemoteKeysDao
import com.newsapp.data.local.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides local database dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        )
        .fallbackToDestructiveMigration(false)
        .build()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(database: NewsDatabase): BookmarkDao {
        return database.bookmarkDao
    }

    @Provides
    @Singleton
    fun provideNewsArticleDao(database: NewsDatabase): NewsArticleDao {
        return database.newsArticleDao
    }

    @Provides
    @Singleton
    fun provideNewsRemoteKeysDao(database: NewsDatabase): NewsRemoteKeysDao {
        return database.newsRemoteKeysDao
    }
}
