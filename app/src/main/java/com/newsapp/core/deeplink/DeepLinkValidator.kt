package com.newsapp.core.deeplink

import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Validates incoming deep link URIs for security and correctness.
 */
@Singleton
class DeepLinkValidator @Inject constructor() {

    /**
     * Checks if the given URI is a valid and trusted deep link for the app.
     */
    fun isValid(uri: Uri): Boolean {
        val scheme = uri.scheme ?: return false
        val host = uri.host
        
        // Support custom scheme: newsapp://...
        if (scheme == LinkConstants.SCHEME_CUSTOM) return true
        
        // Support HTTPS App Links: https://newsapp.com/...
        if (scheme == LinkConstants.SCHEME_HTTPS) {
            return host == LinkConstants.HOST
        }
        
        return false
    }

    /**
     * Sanitizes and validates specific parameters (e.g., article ID).
     */
    fun isValidArticleId(id: String?): Boolean {
        return !id.isNullOrBlank() && id.length > 3
    }
}
