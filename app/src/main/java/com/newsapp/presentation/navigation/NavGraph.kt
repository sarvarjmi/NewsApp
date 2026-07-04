package com.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.newsapp.presentation.bookmarks.BookmarksScreen
import com.newsapp.presentation.detail.DetailScreen
import com.newsapp.presentation.home.HomeScreen
import com.newsapp.presentation.home.HomeViewModel
import com.newsapp.presentation.search.SearchScreen
import com.newsapp.presentation.search.SearchViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = modifier
    ) {
        composable<Route.Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDetail = { url ->
                    navController.navigate(Route.Detail(url))
                },
                onNavigateToSearch = {
                    navController.navigate(Route.Search)
                }
            )
        }
        composable<Route.Search> {
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(
                viewModel = viewModel,
                onNavigateToDetail = { url ->
                    navController.navigate(Route.Detail(url))
                }
            )
        }
        composable<Route.Bookmarks> {
            BookmarksScreen(
                onNavigateToDetail = { url ->
                    navController.navigate(Route.Detail(url))
                }
            )
        }
        composable<Route.Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Route.Detail>()
            DetailScreen(
                articleUrl = detail.articleUrl,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
