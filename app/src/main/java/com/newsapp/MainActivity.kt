package com.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.newsapp.core.dispatcher.DispatcherProvider
import com.newsapp.core.network.NetworkMonitor
import com.newsapp.presentation.common.OfflineBanner
import com.newsapp.presentation.common.SnackbarManager
import com.newsapp.presentation.navigation.Destination
import com.newsapp.presentation.navigation.NavGraph
import com.newsapp.presentation.navigation.NewsBottomBar
import com.newsapp.presentation.navigation.Routes
import com.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val isOnline by networkMonitor.isOnline.collectAsState(initial = true)
                val snackbarHostState = remember { SnackbarHostState() }

                // Listen to global snackbar messages
                LaunchedEffect(Unit) {
                    snackbarManager.messages.collect { message ->
                        val result = snackbarHostState.showSnackbar(
                            message = message.message,
                            actionLabel = message.actionLabel,
                            duration = message.duration
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            message.onAction?.invoke()
                        }
                    }
                }

                // Only show bottom bar for main destinations (Home, Search, Bookmarks)
                val showBottomBar = currentDestination?.hierarchy?.any { 
                    it.hasRoute(Routes.MainGraph::class) 
                } == true

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        OfflineBanner(isVisible = !isOnline)
                    },
                    bottomBar = {
                        if (showBottomBar) {
                            NewsBottomBar(
                                navController = navController,
                                currentDestination = currentDestination
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        isOnline = isOnline,
                        modifier = Modifier.padding(
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    )
                }
            }
        }
    }
}
