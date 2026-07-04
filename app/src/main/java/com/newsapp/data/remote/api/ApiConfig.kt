package com.newsapp.data.remote.api

import javax.inject.Inject
import javax.inject.Singleton

/**
 * High-level configuration for the News API.
 * This class provides a way to access API settings in a structured way.
 */
@Singleton
class ApiConfig @Inject constructor() {
    
    val baseUrl: String = ApiConstants.BASE_URL
    val apiKey: String = ApiConstants.API_KEY
    
    /**
     * The number of articles to fetch per page.
     */
    val defaultPageSize: Int = ApiConstants.DEFAULT_PAGE_SIZE
    
    /**
     * The default country for headlines.
     */
    val defaultCountry: String = ApiConstants.DEFAULT_COUNTRY
    
    /**
     * The default language for articles.
     */
    val defaultLanguage: String = ApiConstants.DEFAULT_LANGUAGE
}
