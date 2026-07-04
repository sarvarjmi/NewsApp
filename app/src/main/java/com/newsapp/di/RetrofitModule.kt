package com.newsapp.di

import com.newsapp.core.network.RetrofitProvider
import com.newsapp.data.remote.api.NewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module responsible for providing Retrofit-related singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    /**
     * Provides a singleton instance of [Retrofit].
     * 
     * It uses [RetrofitProvider] to encapsulate the builder logic, keeping 
     * this module clean and focused on dependency management.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return RetrofitProvider.getRetrofit(okHttpClient)
    }

    /**
     * Provides the [NewsApiService] instance by creating it from the [Retrofit] client.
     */
    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}
