package com.newsapp.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

/**
 * Extension functions for [NavController] to handle standardized navigation patterns.
 */

/**
 * Navigates to a top-level destination with state preservation and single-top behavior.
 * This is the standard pattern for bottom navigation clicks.
 */
fun NavController.navigateToTopLevel(route: Routes) {
    this.navigate(route) {
        popUpTo(this@navigateToTopLevel.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Navigates to a destination and clears the backstack up to the start destination.
 * Useful for deep link entry points.
 */
fun NavController.navigateAndClearBackStack(route: Routes) {
    this.navigate(route) {
        popUpTo(this@navigateAndClearBackStack.graph.id) {
            inclusive = true
        }
    }
}
