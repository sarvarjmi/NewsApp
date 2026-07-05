package com.newsapp.core.performance

import com.newsapp.core.common.Logger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for logging performance-related metrics and events.
 */
@Singleton
class PerformanceLogger @Inject constructor(
    private val logger: Logger
) {
    /**
     * Logs the time taken by a specific operation.
     */
    fun logDuration(operationName: String, timeMillis: Long) {
        logger.d("Performance: $operationName took ${timeMillis}ms", "Performance")
    }

    /**
     * Logs a frame drop event.
     */
    fun logFrameDrop(count: Int) {
        logger.i("Performance: Dropped $count frames", "Performance")
    }
}
