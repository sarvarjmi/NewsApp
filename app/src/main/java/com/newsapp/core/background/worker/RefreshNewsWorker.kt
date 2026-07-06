package com.newsapp.core.background.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.newsapp.core.background.util.WorkerResultMapper
import com.newsapp.domain.usecase.news.RefreshNewsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

/**
 * Worker responsible for refreshing news headlines in the background.
 */
@HiltWorker
class RefreshNewsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Timber.d("RefreshNewsWorker: Starting news refresh")
            refreshNewsUseCase()
            Timber.d("RefreshNewsWorker: News refresh successful")
            Result.success()
        } catch (e: Exception) {
            WorkerResultMapper.mapFailure(e, runAttemptCount)
        }
    }
}
