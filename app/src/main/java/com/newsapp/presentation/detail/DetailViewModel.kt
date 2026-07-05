package com.newsapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.newsapp.domain.model.Article
import com.newsapp.domain.usecase.bookmark.IsBookmarkedUseCase
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the Article Detail screen.
 *
 * It manages the lifecycle of a single article view, handling bookmark persistence,
 * reading time calculation, and external side-effects (Share, Browser).
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val isBookmarkedUseCase: IsBookmarkedUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailUiState, DetailSideEffect>(DetailUiState.initial()) {

    /**
     * Handles incoming UI events from the Detail Screen.
     */
    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnBookmarkToggled -> toggleBookmark(event.article)
            is DetailEvent.OnShareClicked -> sendEffect(DetailSideEffect.ShareArticle(event.article.url, event.article.title))
            is DetailEvent.OnOpenInBrowserClicked -> sendEffect(DetailSideEffect.NavigateToWebView(event.url))
            DetailEvent.OnBackClicked -> sendEffect(DetailSideEffect.NavigateBack)
            DetailEvent.OnRetryClicked -> currentState.article?.let { init(it) }
        }
    }

    /**
     * Initializes the state with an article object and fetches its bookmark status.
     * 
     * @param article The article to display.
     */
    fun init(article: Article?) {
        if (article == null) {
            updateState { it.copy(isLoading = false, error = "Article details unavailable") }
            return
        }

        val readingTime = calculateReadingTime(article.content ?: article.description ?: "")
        
        updateState { 
            it.copy(
                article = article, 
                isLoading = false,
                readingTime = readingTime,
                error = null
            ) 
        }
        
        checkBookmarkStatus(article.url)
    }

    private fun toggleBookmark(article: Article) {
        safeLaunch {
            toggleBookmarkUseCase(article)
            val isNowBookmarked = isBookmarkedUseCase(article.url)
            updateState { it.copy(isBookmarked = isNowBookmarked) }
        }
    }

    private fun checkBookmarkStatus(url: String) {
        safeLaunch {
            val bookmarked = isBookmarkedUseCase(url)
            updateState { it.copy(isBookmarked = bookmarked) }
        }
    }

    /**
     * Business Logic: Simple reading time calculation based on average reading speed (200 wpm).
     */
    private fun calculateReadingTime(content: String): String {
        if (content.isBlank()) return "1 min read"
        val words = content.split("\\s+".toRegex()).size
        val minutes = (words / 200).coerceAtLeast(1)
        return "$minutes min read"
    }
}
