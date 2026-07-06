package com.newsapp.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * A custom Bottom Navigation Bar implementation using Material 3 [NavigationBar].
 * 
 * This component manages its own selection state based on the current navigation destination
 * and handles safe navigation with state restoration.
 *
 * @param navController The [NavHostController] used to handle navigation actions.
 * @param currentDestination The current active destination in the navigation hierarchy.
 */
@Composable
fun NewsBottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        // Iterate through all top-level destinations defined in the Destination sealed class
        Destination.topLevelDestinations.forEach { destination ->
            
            // Check if the current destination or any of its parents match the tab's route
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(destination.route::class)
            } == true

            NavigationBarItem(
                selected = isSelected,
                label = { 
                    Text(text = stringResource(id = destination.labelRes)) 
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = stringResource(id = destination.labelRes)
                    )
                },
                onClick = {
                    // Use centralized extension for top-level navigation
                    navController.navigateToTopLevel(destination.route)
                }
            )
        }
    }
}
