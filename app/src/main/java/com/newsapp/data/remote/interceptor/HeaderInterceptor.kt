package com.newsapp.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor that adds common headers to every network request.
 */
@Singleton
class HeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("User-Agent", "NewsApp/1.0 (Android)")

        // Performance: Only add JSON headers for News API requests.
        // Images and other static assets don't need these.
        if (originalRequest.url.host.contains("newsapi.org")) {
            requestBuilder.addHeader("Accept", "application/json")
            requestBuilder.addHeader("Content-Type", "application/json")
        }
        
        return chain.proceed(requestBuilder.build())
    }
}
