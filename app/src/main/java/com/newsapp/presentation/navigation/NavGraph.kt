package com.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.newsapp.domain.model.Article
import com.newsapp.presentation.bookmarks.BookmarkScreen
import com.newsapp.presentation.bookmarks.BookmarkViewModel
import com.newsapp.presentation.detail.DetailScreen
import com.newsapp.presentation.detail.DetailViewModel
import com.newsapp.presentation.home.HomeScreen
import com.newsapp.presentation.home.HomeViewModel
import com.newsapp.presentation.search.SearchScreen
import com.newsapp.presentation.search.SearchViewModel
import com.newsapp.presentation.webview.WebViewScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.reflect.typeOf

/**
 * The main Navigation Graph for the NewsApp.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MainGraph,
        modifier = modifier
    ) {
        /**
         * Nested Navigation Graph for Bottom Navigation destinations.
         */
        navigation<Routes.MainGraph>(startDestination = Routes.Home) {
            
            composable<Routes.Home>(
                deepLinks = DeepLinks.Home
            ) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = viewModel,
                    isOnline = isOnline,
                    onNavigateToDetail = { article ->
                        navController.navigate(Routes.Detail(article))
                    },
                    onNavigateToSearch = {
                        navController.navigate(Routes.Search())
                    }
                )
            }

            composable<Routes.Search>(
                deepLinks = DeepLinks.Search
            ) { backStackEntry ->
                val searchRoute = backStackEntry.toRoute<Routes.Search>()
                val viewModel: SearchViewModel = hiltViewModel()
                
                LaunchedEffect(searchRoute.query) {
                    viewModel.initQuery(searchRoute.query)
                }

                SearchScreen(
                    viewModel = viewModel,
                    isOnline = isOnline,
                    onNavigateToDetail = { article ->
                        navController.navigate(Routes.Detail(article))
                    }
                )
            }

            composable<Routes.Bookmarks>(
                deepLinks = DeepLinks.Bookmarks
            ) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                BookmarkScreen(
                    onNavigateToDetail = { article ->
                        navController.navigate(Routes.Detail(article))
                    },
                    viewModel = viewModel
                )
            }
        }

        composable<Routes.Detail>(
            typeMap = mapOf(typeOf<Article>() to ArticleNavType)
        ) { backStackEntry ->
            val detail = backStackEntry.toRoute<Routes.Detail>()
            DetailScreen(
                article = detail.article,
                onBackClick = { navController.navigateUp() },
                onNavigateToWebView = { url ->
                    navController.navigate(Routes.WebView(url))
                }
            )
        }

        composable<Routes.ArticleDeepLink>(
            deepLinks = DeepLinks.ArticleDetail
        ) { backStackEntry ->
            val deepLink = backStackEntry.toRoute<Routes.ArticleDeepLink>()
            val viewModel: DetailViewModel = hiltViewModel()
            
            LaunchedEffect(deepLink.url) {
                viewModel.initWithUrl(deepLink.url)
            }

            DetailScreen(
                onBackClick = { navController.navigateUp() },
                onNavigateToWebView = { url ->
                    navController.navigate(Routes.WebView(url))
                },
                viewModel = viewModel
            )
        }

        composable<Routes.WebView>(
            deepLinks = DeepLinks.WebView
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
