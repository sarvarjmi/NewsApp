package com.newsapp.data.remote.api

import com.newsapp.BuildConfig

/**
 * Constants related to the News API.
 */
object ApiConstants {
    /**
     * Base URL for the News API.
     */
    const val BASE_URL = BuildConfig.BASE_URL
    
    /**
     * API Key for the News API. 
     */
    const val API_KEY = BuildConfig.API_KEY

    /**
     * Default query parameters
     */
    const val DEFAULT_COUNTRY = "us"
    const val DEFAULT_PAGE_SIZE = 20
    const val DEFAULT_LANGUAGE = "en"
}
