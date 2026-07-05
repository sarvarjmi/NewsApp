package com.newsapp.core.error

import com.newsapp.core.common.Logger
import com.newsapp.presentation.common.SnackbarManager
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A central handler for uncaught coroutine exceptions across the application.
 *
 * It provides a [CoroutineExceptionHandler] that logs the error and optionally
 * notifies the user via the [SnackbarManager].
 */
@Singleton
class GlobalErrorHandler @Inject constructor(
    private val snackbarManager: SnackbarManager,
    private val errorMapper: ErrorMapper,
    private val logger: Logger
) {
    /**
     * A standard [CoroutineExceptionHandler] to be used in [CoroutineScope]s.
     */
    val handler = CoroutineExceptionHandler { _, throwable ->
        val appError = errorMapper.map(throwable)
        
        // 1. Log for developers and remote tracking
        logger.e(throwable, "GlobalErrorHandler: Uncaught exception")
        logger.recordException(throwable)

        // 2. Map to user-friendly message
        // This is a global fallback; specific errors should be handled in ViewModels
    }
}
