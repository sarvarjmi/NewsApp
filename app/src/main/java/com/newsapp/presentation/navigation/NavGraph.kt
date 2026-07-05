package com.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.newsapp.presentation.bookmarks.BookmarksScreen
import com.newsapp.presentation.detail.DetailScreen
import com.newsapp.presentation.home.HomeScreen
import com.newsapp.presentation.home.HomeViewModel
import com.newsapp.presentation.search.SearchScreen
import com.newsapp.presentation.search.SearchViewModel
import com.newsapp.presentation.webview.WebViewScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * The main Navigation Graph for the NewsApp.
 * 
 * This graph coordinates all screen transitions, argument passing, and deep link handling.
 * It uses a single [NavHost] with nested navigation for the primary features.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MainGraph,
        modifier = modifier
    ) {
        /**
         * Nested Navigation Graph for Bottom Navigation destinations.
         * Grouping these allows for better shared logic and state management.
         */
        navigation<Routes.MainGraph>(startDestination = Routes.Home) {
            
            // Home Destination
            composable<Routes.Home>(
                deepLinks = listOf(
                    navDeepLink<Routes.Home>(basePath = "newsapp://home")
                )
            ) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { url ->
                        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail(encodedUrl))
                    },
                    onNavigateToSearch = {
                        navController.navigate(Routes.Search)
                    }
                )
            }

            // Search Destination
            composable<Routes.Search>(
                deepLinks = listOf(
                    navDeepLink<Routes.Search>(basePath = "newsapp://search")
                )
            ) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { url ->
                        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail(encodedUrl))
                    }
                )
            }

            // Bookmarks Destination
            composable<Routes.Bookmarks>(
                deepLinks = listOf(
                    navDeepLink<Routes.Bookmarks>(basePath = "newsapp://bookmarks")
                )
            ) {
                BookmarksScreen(
                    onNavigateToDetail = { url ->
                        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail(encodedUrl))
                    }
                )
            }
        }

        /**
         * Article Detail Screen.
         * Accepts an encoded article URL to load the specific news content.
         */
        composable<Routes.Detail>(
            deepLinks = listOf(
                navDeepLink<Routes.Detail>(basePath = "newsapp://article")
            )
        ) { backStackEntry ->
            val detail = backStackEntry.toRoute<Routes.Detail>()
            val decodedUrl = URLDecoder.decode(detail.articleUrl, StandardCharsets.UTF_8.toString())
            DetailScreen(
                articleUrl = decodedUrl,
                onBackClick = { navController.navigateUp() }
            )
        }

        /**
         * WebView Screen for original source viewing.
         */
        composable<Routes.WebView>(
            deepLinks = listOf(
                navDeepLink<Routes.WebView>(basePath = "newsapp://webview")
            )
        ) { backStackEntry ->
            val webView = backStackEntry.toRoute<Routes.WebView>()
            val decodedUrl = URLDecoder.decode(webView.url, StandardCharsets.UTF_8.toString())
            WebViewScreen(
                url = decodedUrl,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
