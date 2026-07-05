package com.newsapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.newsapp.domain.model.Article
import com.newsapp.domain.usecase.bookmark.IsBookmarkedUseCase
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
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
            is DetailEvent.OnOpenInBrowserClicked -> sendEffect(DetailSideEffect.OpenBrowser(event.url))
            DetailEvent.OnBackClicked -> sendEffect(DetailSideEffect.NavigateBack)
            DetailEvent.OnRetryClicked -> loadArticleMetadata()
        }
    }

    /**
     * Initializes the state with an article object and fetches its bookmark status.
     * 
     * @param article The article to display.
     */
    fun init(article: Article?) {
        if (article == null) {
            updateState { it.copy(isLoading = false, error = "Article not found") }
            return
        }

        val readingTime = calculateReadingTime(article.content ?: "")
        
        updateState { 
            it.copy(
                article = article, 
                isLoading = false,
                readingTime = readingTime
            ) 
        }
        
        checkBookmarkStatus(article.url)
    }

    private fun toggleBookmark(article: Article) {
        safeLaunch {
            toggleBookmarkUseCase(article)
            // Reactively update the local state flag
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

    private fun loadArticleMetadata() {
        // Implementation for deep-link data recovery (Future phase)
    }

    /**
     * Business Logic: Simple reading time calculation based on average reading speed (200 wpm).
     */
    private fun calculateReadingTime(content: String): String {
        val words = content.split(" ").size
        val minutes = (words / 200).coerceAtLeast(1)
        return "$minutes min read"
    }
}
