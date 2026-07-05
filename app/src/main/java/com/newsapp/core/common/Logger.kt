package com.newsapp.core.common

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for application-wide logging.
 * Decouples the application from specific logging implementations (like Timber or Crashlytics).
 */
interface Logger {
    fun d(message: String, tag: String? = null)
    fun e(throwable: Throwable, message: String? = null)
    fun i(message: String, tag: String? = null)
    fun recordException(throwable: Throwable)
}

/**
 * Timber-based implementation of the [Logger].
 */
@Singleton
class AppLogger @Inject constructor() : Logger {
    override fun d(message: String, tag: String?) {
        tag?.let { Timber.tag(it).d(message) } ?: Timber.d(message)
    }

    override fun e(throwable: Throwable, message: String?) {
        message?.let { Timber.e(throwable, it) } ?: Timber.e(throwable)
    }

    override fun i(message: String, tag: String?) {
        tag?.let { Timber.tag(it).i(message) } ?: Timber.i(message)
    }

    override fun recordException(throwable: Throwable) {
        // Here we would integrate with Firebase Crashlytics
        // FirebaseCrashlytics.getInstance().recordException(throwable)
        Timber.e(throwable, "Non-fatal exception recorded")
    }
}
