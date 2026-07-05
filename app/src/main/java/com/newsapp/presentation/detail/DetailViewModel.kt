package com.newsapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.newsapp.domain.model.Article
import com.newsapp.domain.usecase.bookmark.ObserveBookmarkStatusUseCase
import com.newsapp.domain.usecase.bookmark.ToggleBookmarkUseCase
import com.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
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
    private val observeBookmarkStatusUseCase: ObserveBookmarkStatusUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailUiState, DetailSideEffect>(DetailUiState.initial()) {

    private var bookmarkJob: Job? = null

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
        
        observeBookmarkStatus(article.url)
    }

    private fun toggleBookmark(article: Article) {
        safeLaunch {
            toggleBookmarkUseCase(article)
        }
    }

    private fun observeBookmarkStatus(url: String) {
        bookmarkJob?.cancel()
        bookmarkJob = safeLaunch {
            observeBookmarkStatusUseCase(url).collectLatest { isBookmarked ->
                updateState { it.copy(isBookmarked = isBookmarked) }
            }
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
