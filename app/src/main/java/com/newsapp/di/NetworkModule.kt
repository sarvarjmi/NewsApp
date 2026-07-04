package com.newsapp.di

import com.newsapp.core.network.SerializationConfig
import com.newsapp.data.remote.api.ApiConstants
import com.newsapp.data.remote.api.NewsApiService
import com.newsapp.data.remote.interceptor.ApiKeyInterceptor
import com.newsapp.data.remote.interceptor.ConnectivityInterceptor
import com.newsapp.data.remote.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module that provides networking dependencies including OkHttp and Retrofit.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT_SECONDS = 30L

    /**
     * Provides the logging interceptor. Only enabled in debug builds.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // Ideally check BuildConfig.DEBUG here
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Provides the singleton OkHttpClient instance.
     * Includes:
     * - Timeouts (Connect, Read, Write)
     * - Interceptors (Logging, API Key, Connectivity, Headers)
     * - Retry on connection failure
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor,
        connectivityInterceptor: ConnectivityInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * Provides the Retrofit instance configured with the custom OkHttpClient.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(SerializationConfig.json.asConverterFactory(contentType))
            .build()
    }

    /**
     * Provides the NewsApiService implementation.
     */
    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}
