package com.newsapp.core.network

import kotlinx.serialization.json.Json

/**
 * Configuration for Kotlinx Serialization.
 */
object SerializationConfig {
    /**
     * Pre-configured Json instance for the application.
     * - ignoreUnknownKeys: true (Handles new API fields gracefully)
     * - isLenient: true (Handles slightly malformed JSON)
     * - encodeDefaults: true (Includes default values in serialized JSON)
     * - coerceInputValues: true (Handles missing/null fields by using defaults)
     */
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        coerceInputValues = true
    }
}
