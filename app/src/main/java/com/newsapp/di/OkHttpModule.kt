package com.newsapp.di

import android.content.Context
import com.newsapp.core.performance.PerformanceConfig
import com.newsapp.data.remote.interceptor.ApiKeyInterceptor
import com.newsapp.data.remote.interceptor.ConnectivityInterceptor
import com.newsapp.data.remote.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module that provides OkHttp networking dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {

    private const val TIMEOUT_SECONDS = 30L

    /**
     * Provides the logging interceptor for standard debug logging.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Provides the singleton OkHttpClient instance.
     * 
     * Configures the client with:
     * - Connection, Read, and Write timeouts of 30 seconds.
     * - A suite of custom interceptors for connectivity, authentication, and headers.
     * - Automatic retry on connection failures.
     * - Disk cache for optimized network usage.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor,
        connectivityInterceptor: ConnectivityInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val cache = Cache(context.cacheDir.resolve("http_cache"), PerformanceConfig.NETWORK_CACHE_SIZE)

        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }
}
