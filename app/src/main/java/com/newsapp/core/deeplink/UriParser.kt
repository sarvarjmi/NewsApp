package com.newsapp.core.deeplink

import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Low-level utility for extracting raw data from URIs.
 */
@Singleton
class UriParser @Inject constructor() {

    /**
     * Extracts the path segments from a URI.
     */
    fun getPathSegments(uri: Uri): List<String> = uri.pathSegments

    /**
     * Extracts a query parameter by name.
     */
    fun getQueryParameter(uri: Uri, name: String): String? = uri.getQueryParameter(name)
}
