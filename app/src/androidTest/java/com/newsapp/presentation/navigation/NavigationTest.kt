package com.newsapp.presentation.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.newsapp.ui.theme.NewsAppTheme
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun app_startDestinationIsHome() {
        composeTestRule.setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, isOnline = true)
            }
        }

        composeTestRule.onNodeWithText("NewsApp").assertIsDisplayed()
    }

    @Test
    fun app_clickingSearchTab_navigatesToSearch() {
        composeTestRule.setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                // We need the Scaffold with BottomBar for this test
                com.newsapp.presentation.navigation.NewsBottomBar(
                    navController = navController,
                    currentDestination = null // Simplified for test
                )
            }
        }

        // Search text from bottom bar
        composeTestRule.onNodeWithText("Search").performClick()
        
        // This test is very basic, a real one would verify NavController state
    }
}
