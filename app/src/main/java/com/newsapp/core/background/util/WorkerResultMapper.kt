package com.newsapp.core.background.util

import androidx.work.ListenableWorker.Result
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * Utility to map application-level errors or results to WorkManager [Result]s.
 *
 * This helps maintain consistency in how background tasks respond to different
 * failure scenarios.
 */
object WorkerResultMapper {

    /**
     * Maps a [Throwable] to a WorkManager [Result].
     * 
     * @param throwable The exception encountered during work.
     * @param runAttemptCount The current attempt count provided by the worker.
     * @return [Result.retry] for recoverable errors, [Result.failure] otherwise.
     */
    fun mapFailure(throwable: Throwable, runAttemptCount: Int): Result {
        Timber.e(throwable, "Worker failure detected (Attempt: $runAttemptCount)")
        
        return when (throwable) {
            // Retry on network-related issues (Timeouts, connectivity lost)
            is IOException -> {
                if (runAttemptCount < 3) Result.retry() else Result.failure()
            }
            
            // Handle API specific errors
            is HttpException -> {
                when (throwable.code()) {
                    // Retry on temporary server issues or rate limiting
                    429, 500, 503, 504 -> {
                        if (runAttemptCount < 3) Result.retry() else Result.failure()
                    }
                    // Fail permanently on auth or client errors
                    else -> Result.failure()
                }
            }

            else -> Result.failure()
        }
    }
}
