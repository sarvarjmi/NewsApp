package com.newsapp.core.network

import com.newsapp.data.remote.api.ApiConstants
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * A centralized provider for the Retrofit client.
 * 
 * This class encapsulates the creation logic of the Retrofit instance, 
 * making it easy to maintain and test without cluttering Hilt modules.
 */
object RetrofitProvider {

    /**
     * Configures and builds a [Retrofit] instance.
     * 
     * @param okHttpClient The client used for network requests, typically 
     * configured with interceptors, timeouts, and logging.
     * @return A singleton-ready [Retrofit] instance.
     */
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        // Defines the content type for JSON responses
        val contentType = "application/json".toMediaType()
        
        return Retrofit.Builder()
            // Sets the API's base URL (e.g., https://newsapi.org/v2/)
            .baseUrl(ApiConstants.BASE_URL)
            
            // Attaches the custom OkHttpClient instance for advanced request handling
            .client(okHttpClient)
            
            // Integrates Kotlinx Serialization as the primary JSON converter.
            // This ensures strict type safety and handles Kotlin features like nullability.
            .addConverterFactory(
                SerializationConfig.json.asConverterFactory(contentType)
            )
            
            .build()
    }
}
