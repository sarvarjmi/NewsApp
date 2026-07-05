package com.newsapp.presentation.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A central manager for handling Snackbars across the application.
 *
 * This utility allows ViewModels to trigger notifications without having a direct 
 * dependency on the Compose UI state or [SnackbarHostState].
 */
@Singleton
class SnackbarManager @Inject constructor() {

    private val _messages = MutableSharedFlow<SnackbarMessage>()
    val messages = _messages.asSharedFlow()

    /**
     * Dispatches a message to be shown in the active SnackbarHost.
     */
    suspend fun showMessage(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onAction: (() -> Unit)? = null
    ) {
        _messages.emit(
            SnackbarMessage(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
                onAction = onAction
            )
        )
    }
}

/**
 * Represents the data required to show a single Snackbar.
 */
data class SnackbarMessage(
    val message: String,
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onAction: (() -> Unit)? = null
)
