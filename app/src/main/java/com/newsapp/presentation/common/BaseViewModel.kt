package com.newsapp.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A reusable base class for all ViewModels in the NewsApp project.
 *
 * This class implements the Unidirectional Data Flow (UDF) pattern, providing
 * standardized state management, side-effect handling, and robust coroutine execution.
 *
 * @param S The type representing the UI State (must be an immutable data class).
 * @param E The type representing UI Effects (one-time events like navigation or Snackbars).
 */
abstract class BaseViewModel<S, E>(initialState: S) : ViewModel() {

    /**
     * Internal mutable state flow that holds the current UI state.
     */
    private val _state = MutableStateFlow(initialState)

    /**
     * Publicly exposed read-only state flow for the UI to observe.
     */
    val state = _state.asStateFlow()

    /**
     * Internal mutable shared flow for one-time events (Effects).
     * We use SharedFlow instead of StateFlow for events to ensure they aren't
     * re-delivered on configuration changes (like screen rotation).
     */
    private val _effect = MutableSharedFlow<E>()

    /**
     * Publicly exposed read-only shared flow for UI side-effects.
     */
    val effect = _effect.asSharedFlow()

    /**
     * A helper property to access the current value of the state.
     */
    protected val currentState: S get() = state.value

    /**
     * Standardized Coroutine Exception Handler.
     * Logs the error using Timber and provides a hook for subclasses to handle specific failures.
     */
    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "Coroutine failure in ${this.javaClass.simpleName}")
        handleError(exception)
    }

    /**
     * Updates the UI state atomically using the previous state as input.
     *
     * @param reducer A lambda function that transforms the old state into the new state.
     */
    protected fun updateState(reducer: (S) -> S) {
        _state.update(reducer)
    }

    /**
     * Dispatches a one-time UI effect (e.g., triggering a navigation action).
     *
     * @param effect The side-effect to be emitted.
     */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    /**
     * Executes a coroutine within the [viewModelScope] with automatic exception handling.
     *
     * @param context Additional coroutine context (defaults to Empty).
     * @param block The suspendable work to be executed.
     */
    protected fun safeLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(handler + context) {
            block()
        }
    }

    /**
     * Hook for subclasses to implement custom error handling logic.
     * This is triggered by the [handler] whenever a coroutine in [safeLaunch] fails.
     *
     * @param throwable The caught exception.
     */
    protected open fun handleError(throwable: Throwable) {
        // Subclasses should override this to update state with an error message or send an error effect.
    }

    /**
     * Executes a coroutine with automatic loading management.
     * 
     * @param loadingState A lambda to update the loading flag in the subclass UI state.
     * @param block The suspendable work to execute.
     */
    protected fun launchWithLoading(
        loadingState: (Boolean) -> Unit,
        block: suspend CoroutineScope.() -> Unit
    ) {
        safeLaunch {
            try {
                loadingState(true)
                block()
            } finally {
                loadingState(false)
            }
        }
    }
}
