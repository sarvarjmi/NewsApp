package com.newsapp.presentation.navigation

import androidx.navigation.navDeepLink

/**
 * Centralized deep link definitions for use in the Navigation Graph.
 */
object DeepLinks {
    
    val Home = listOf(
        navDeepLink<Routes.Home>(basePath = "https://newsapp.com"),
        navDeepLink<Routes.Home>(basePath = "https://newsapp.com/home"),
        navDeepLink<Routes.Home>(basePath = "newsapp://home")
    )
    
    val Search = listOf(
        navDeepLink<Routes.Search>(basePath = "https://newsapp.com/search"),
        navDeepLink<Routes.Search>(basePath = "newsapp://search")
    )
    
    val Bookmarks = listOf(
        navDeepLink<Routes.Bookmarks>(basePath = "https://newsapp.com/bookmarks"),
        navDeepLink<Routes.Bookmarks>(basePath = "newsapp://bookmarks")
    )
    
    val ArticleDetail = listOf(
        navDeepLink<Routes.ArticleDeepLink>(basePath = "https://newsapp.com/article"),
        navDeepLink<Routes.ArticleDeepLink>(basePath = "newsapp://article")
    )
    
    val WebView = listOf(
        navDeepLink<Routes.WebView>(basePath = "newsapp://webview")
    )
}
