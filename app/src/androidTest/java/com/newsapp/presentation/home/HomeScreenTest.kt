package com.newsapp.presentation.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.domain.usecase.news.GetTopHeadlinesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase = mockk(relaxed = true)
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk(relaxed = true)
    
    // We mock the ViewModel to control the state
    private val viewModel: HomeViewModel = mockk(relaxed = true)

    @Test
    fun homeScreen_shouldDisplayTitleAndSearchIcon() {
        val state = HomeUiState(
            categories = listOf("general", "business"),
            selectedCategory = "general"
        )
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.articles } returns flowOf() // Paging items usually need more setup for full testing

        composeTestRule.setContent {
            HomeScreen(
                viewModel = viewModel,
                isOnline = true,
                isDarkMode = false,
                onToggleTheme = {},
                onNavigateToDetail = {},
                onNavigateToSearch = {}
            )
        }

        composeTestRule.onNodeWithText("NewsApp").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Search News").assertIsDisplayed()
    }

    @Test
    fun homeScreen_clickingSearchIcon_shouldTriggerEvent() {
        val state = HomeUiState()
        every { viewModel.state } returns MutableStateFlow(state)
        every { viewModel.articles } returns flowOf()

        composeTestRule.setContent {
            HomeScreen(
                viewModel = viewModel,
                isOnline = true,
                isDarkMode = false,
                onToggleTheme = {},
                onNavigateToDetail = {},
                onNavigateToSearch = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Search News").performClick()
        
        verify { viewModel.onEvent(HomeEvent.OnSearchClicked) }
    }
}
