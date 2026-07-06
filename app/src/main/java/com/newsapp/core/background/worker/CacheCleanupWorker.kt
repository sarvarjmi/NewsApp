package com.newsapp.core.background.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.newsapp.data.local.datasource.LocalNewsDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import timber.log.Timber

/**
 * Worker responsible for cleaning up old cached articles in the background.
 */
@HiltWorker
class CacheCleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val localDataSource: LocalNewsDataSource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Timber.d("CacheCleanupWorker: Starting cache cleanup")
            
            // Define the expiration threshold (7 days ago)
            val threshold = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
            
            // Delete articles older than the threshold
            localDataSource.deleteOldArticles(threshold)
            
            Timber.d("CacheCleanupWorker: Cache cleanup successful")
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "CacheCleanupWorker: Cache cleanup failed")
            Result.failure()
        }
    }
}
