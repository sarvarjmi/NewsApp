package com.newsapp.data.remote.interceptor

import com.newsapp.data.remote.api.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor that automatically appends the API key to every request's query parameters.
 */
@Singleton
class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Performance & Bug Fix: Only add the API key to requests targeting the News API host.
        // Adding it to image requests (e.g. apnews.com) can cause those servers to reject the request.
        if (!originalUrl.host.contains("newsapi.org")) {
            return chain.proceed(originalRequest)
        }

        val url = originalUrl.newBuilder()
            .addQueryParameter("apiKey", ApiConstants.API_KEY)
            .build()

        val requestBuilder = originalRequest.newBuilder()
            .url(url)

        return chain.proceed(requestBuilder.build())
    }
}
