package com.newsapp.core.deeplink

import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility for generating standardized deep links and app links for sharing.
 */
@Singleton
class LinkGenerator @Inject constructor() {

    /**
     * Generates a shareable HTTPS App Link for a specific article.
     * 
     * Format: https://newsapp.com/article/{encoded_url}
     */
    fun generateArticleLink(articleUrl: String): String {
        return Uri.Builder()
            .scheme(LinkConstants.SCHEME_HTTPS)
            .authority(LinkConstants.HOST)
            .appendPath(LinkConstants.PATH_ARTICLE)
            .appendPath(articleUrl) // Builder.appendPath handles encoding automatically
            .build()
            .toString()
    }

    /**
     * Generates a shareable HTTPS App Link for a search query.
     *
     * Format: https://newsapp.com/search?q={query}
     */
    fun generateSearchLink(query: String): String {
        return Uri.Builder()
            .scheme(LinkConstants.SCHEME_HTTPS)
            .authority(LinkConstants.HOST)
            .appendPath(LinkConstants.PATH_SEARCH)
            .appendQueryParameter(LinkConstants.PARAM_QUERY, query)
            .build()
            .toString()
    }
}
