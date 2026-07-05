package com.newsapp.presentation.bookmarks

import androidx.lifecycle.viewModelScope
import com.newsapp.domain.model.Article
import com.newsapp.domain.usecase.bookmark.AddBookmarkUseCase
import com.newsapp.domain.usecase.bookmark.ObserveBookmarksUseCase
import com.newsapp.domain.usecase.bookmark.RemoveBookmarkUseCase
import com.newsapp.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * ViewModel for the Bookmark Screen.
 *
 * This class coordinates the local database operations for saved articles and 
 * manages the UI state of the bookmark list. It provides reactive updates when 
 * articles are added or removed and handles one-time side effects like navigation 
 * and undo actions.
 */
@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val observeBookmarksUseCase: ObserveBookmarksUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase
) : BaseViewModel<BookmarkUiState, BookmarkSideEffect>(BookmarkUiState.initial()) {

    init {
        startObservingBookmarks()
    }

    /**
     * Primary entry point for UI interactions.
     * 
     * Translates UI [BookmarkEvent]s into business logic calls and state updates.
     */
    fun onEvent(event: BookmarkEvent) {
        when (event) {
            is BookmarkEvent.OnDeleteIntent -> {
                updateState { it.copy(articleToDelete = event.article) }
            }
            BookmarkEvent.OnConfirmDelete -> {
                currentState.articleToDelete?.let { article ->
                    removeBookmark(article)
                }
                updateState { it.copy(articleToDelete = null) }
            }
            BookmarkEvent.OnDismissDeleteDialog -> {
                updateState { it.copy(articleToDelete = null) }
            }
            is BookmarkEvent.OnUndoRemove -> undoRemove(event.article)
            is BookmarkEvent.OnArticleClicked -> navigateToDetail(event.article)
            BookmarkEvent.OnRefresh -> refreshBookmarks()
            BookmarkEvent.OnRetryClicked -> startObservingBookmarks()
        }
    }

    /**
     * Initializes the reactive observation of the bookmarks database.
     * Updates the state automatically whenever the database content changes.
     */
    private fun startObservingBookmarks() {
        updateState { it.copy(isLoading = true, error = null) }
        
        observeBookmarksUseCase()
            .onEach { articles ->
                updateState { 
                    it.copy(
                        articles = articles,
                        bookmarkCount = articles.size,
                        isLoading = false,
                        isRefreshing = false
                    ) 
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Deletes an article from the local database.
     * Triggers an "Undo" side effect for the UI to display a Snackbar.
     */
    private fun removeBookmark(article: Article) {
        safeLaunch {
            removeBookmarkUseCase(article)
            sendEffect(BookmarkSideEffect.ShowUndoSnackbar(article))
        }
    }

    /**
     * Re-inserts a previously removed article into the database.
     * Typically called when the user taps "Undo" in the removal Snackbar.
     */
    private fun undoRemove(article: Article) {
        safeLaunch {
            addBookmarkUseCase(article)
        }
    }

    /**
     * Emits a navigation effect to open the article detail screen.
     */
    private fun navigateToDetail(article: Article) {
        sendEffect(BookmarkSideEffect.NavigateToDetail(article))
    }

    /**
     * Simulates a refresh operation.
     * 
     * Since bookmarks are local-first and reactive, a manual refresh primarily 
     * ensures the UI state is synced with the latest database emission.
     */
    private fun refreshBookmarks() {
        safeLaunch {
            updateState { it.copy(isRefreshing = true) }
            // Simulating a minor delay to provide visual feedback for the user
            delay(500)
            // The existing flow observation will handle the data delivery
            updateState { it.copy(isRefreshing = false) }
        }
    }
    
    /**
     * Specialized error handler for Bookmark database exceptions.
     */
    override fun handleError(throwable: Throwable) {
        updateState { it.copy(
            isLoading = false,
            isRefreshing = false,
            error = "Database error: ${throwable.localizedMessage}"
        ) }
    }
}
