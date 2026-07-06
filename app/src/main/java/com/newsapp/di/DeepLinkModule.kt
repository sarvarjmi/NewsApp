package com.newsapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for deep link related dependencies.
 * 
 * Since all these classes use @Inject constructor and @Singleton, 
 * Hilt can provide them without explicit @Provides methods.
 * This module is kept for architectural clarity.
 */
@Module
@InstallIn(SingletonComponent::class)
object DeepLinkModule {
    // No explicit @Provides needed for classes with @Inject constructor
}
