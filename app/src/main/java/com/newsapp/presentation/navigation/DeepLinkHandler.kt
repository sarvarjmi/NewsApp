package com.newsapp.presentation.navigation

import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrates the processing and navigation of deep links.
 */
@Singleton
class DeepLinkHandler @Inject constructor(
    private val routeParser: RouteParser
) {

    /**
     * Extracts deep link data from an [Intent] and navigates to the target route.
     * 
     * @param intent The incoming intent.
     * @param navController The [NavController] used for routing.
     */
    fun handle(intent: Intent, navController: NavController) {
        val data: Uri = intent.data ?: return
        
        Timber.d("Processing Deep Link: $data")
        
        val route = routeParser.parse(data)
        
        if (route != null) {
            Timber.i("Navigating to deep link route: $route")
            
            val destination = Destination.fromRoute(route)
            if (destination != null) {
                navController.navigateToTopLevel(route)
            } else {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        } else {
            Timber.w("Unsupported or invalid deep link: $data")
        }
    }
}
