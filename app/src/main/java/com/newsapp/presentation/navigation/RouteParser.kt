package com.newsapp.presentation.navigation

import android.net.Uri
import com.newsapp.core.deeplink.DeepLinkValidator
import com.newsapp.core.deeplink.LinkConstants
import com.newsapp.core.deeplink.UriParser
import javax.inject.Inject
import javax.inject.Singleton

/**
 * High-level parser that maps validated URIs to application [Routes].
 */
@Singleton
class RouteParser @Inject constructor(
    private val validator: DeepLinkValidator,
    private val uriParser: UriParser
) {

    /**
     * Parses a deep link URI and returns the target [Routes] destination.
     * Returns null if the URI is invalid or unsupported.
     */
    fun parse(uri: Uri): Routes? {
        if (!validator.isValid(uri)) return null

        val scheme = uri.scheme
        val pathSegments = uriParser.getPathSegments(uri)
        
        // Resolve command: For HTTPS it's the first path segment. 
        // For custom scheme, it's either the host or the first path segment.
        val command = if (scheme == LinkConstants.SCHEME_CUSTOM) {
            uri.host ?: pathSegments.getOrNull(0)
        } else {
            pathSegments.getOrNull(0)
        }

        if (command == null) return Routes.Home

        return when (command) {
            LinkConstants.PATH_ARTICLE -> {
                // If it's custom scheme newsapp://article/URL, the URL is the first path segment
                // If it's HTTPS https://domain/article/URL, the URL is the second path segment
                val articleUrl = if (scheme == LinkConstants.SCHEME_CUSTOM && uri.host == LinkConstants.PATH_ARTICLE) {
                    pathSegments.getOrNull(0)
                } else {
                    pathSegments.getOrNull(1)
                }
                
                if (validator.isValidArticleId(articleUrl)) {
                    val decodedUrl = Uri.decode(articleUrl)
                    Routes.ArticleDeepLink(decodedUrl)
                } else null
            }
            
            LinkConstants.PATH_SEARCH -> {
                val query = uriParser.getQueryParameter(uri, LinkConstants.PARAM_QUERY)
                Routes.Search(query)
            }
            
            LinkConstants.PATH_BOOKMARKS -> Routes.Bookmarks

            LinkConstants.PATH_HOME -> Routes.Home
            
            else -> Routes.Home
        }
    }
}
