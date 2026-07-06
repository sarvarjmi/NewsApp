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

        val pathSegments = uriParser.getPathSegments(uri)
        if (pathSegments.isEmpty()) return Routes.Home

        return when (pathSegments[0]) {
            LinkConstants.PATH_ARTICLE -> {
                val encodedUrl = pathSegments.getOrNull(1)
                if (validator.isValidArticleId(encodedUrl)) {
                    val decodedUrl = Uri.decode(encodedUrl)
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
