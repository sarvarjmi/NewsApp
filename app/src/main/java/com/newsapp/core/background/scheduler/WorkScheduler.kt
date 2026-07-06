package com.newsapp.core.background.scheduler

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.newsapp.core.background.worker.AnalyticsWorker
import com.newsapp.core.background.worker.BookmarkBackupWorker
import com.newsapp.core.background.worker.CacheCleanupWorker
import com.newsapp.core.background.worker.NotificationWorker
import com.newsapp.core.background.worker.RefreshNewsWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized scheduler for managing background tasks via WorkManager.
 *
 * This class encapsulates the logic for creating work requests, setting 
 * constraints, and ensuring unique work execution to prevent redundant tasks.
 */
@Singleton
class WorkScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    companion object {
        const val NEWS_REFRESH_WORK_NAME = "NewsRefreshWork"
        const val ANALYTICS_SYNC_WORK_NAME = "AnalyticsSyncWork"
        const val BOOKMARK_BACKUP_WORK_NAME = "BookmarkBackupWork"
        const val NOTIFICATION_SYNC_WORK_NAME = "NotificationSyncWork"
        const val CACHE_CLEANUP_WORK_NAME = "CacheCleanupWork"
        
        private const val REFRESH_INTERVAL_HOURS = 1L
        private const val NOTIFICATION_INTERVAL_MINUTES = 30L
        private const val BACKOFF_DELAY_MINUTES = 10L
    }

    /**
     * Schedules a periodic background task to refresh news headlines.
     */
    fun schedulePeriodicNewsRefresh() {
        val request = PeriodicWorkRequestBuilder<RefreshNewsWorker>(
            REFRESH_INTERVAL_HOURS, TimeUnit.HOURS,
            15, TimeUnit.MINUTES // Flex interval
        )
            .setConstraints(WorkConstraints.default)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                BACKOFF_DELAY_MINUTES,
                TimeUnit.MINUTES
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            NEWS_REFRESH_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Schedules an immediate, one-time background task to refresh news headlines.
     */
    fun scheduleOneTimeNewsRefresh() {
        val request = OneTimeWorkRequestBuilder<RefreshNewsWorker>()
            .setConstraints(WorkConstraints.default)
            .build()

        workManager.enqueueUniqueWork(
            NEWS_REFRESH_WORK_NAME + "_OneTime",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    /**
     * Schedules a periodic task for uploading analytics.
     */
    fun schedulePeriodicAnalyticsSync() {
        val request = PeriodicWorkRequestBuilder<AnalyticsWorker>(
            24, TimeUnit.HOURS,
            1, TimeUnit.HOURS // Flex interval
        )
            .setConstraints(WorkConstraints.maintenance)
            .build()

        workManager.enqueueUniquePeriodicWork(
            ANALYTICS_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Schedules a periodic task for checking breaking news.
     */
    fun schedulePeriodicNotificationCheck() {
        val request = PeriodicWorkRequestBuilder<NotificationWorker>(
            NOTIFICATION_INTERVAL_MINUTES, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES // Flex interval
        )
            .setConstraints(WorkConstraints.default)
            .build()

        workManager.enqueueUniquePeriodicWork(
            NOTIFICATION_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Schedules a periodic task to clean up old cached articles.
     */
    fun schedulePeriodicCacheCleanup() {
        val request = PeriodicWorkRequestBuilder<CacheCleanupWorker>(
            24, TimeUnit.HOURS,
            1, TimeUnit.HOURS // Flex interval
        )
            .setConstraints(WorkConstraints.maintenance)
            .build()

        workManager.enqueueUniquePeriodicWork(
            CACHE_CLEANUP_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Triggers an immediate, one-time backup of bookmarks.
     */
    fun runOneTimeBookmarkBackup() {
        val request = OneTimeWorkRequestBuilder<BookmarkBackupWorker>()
            .setConstraints(WorkConstraints.default)
            .build()

        workManager.enqueueUniqueWork(
            BOOKMARK_BACKUP_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    /**
     * Provides a stream of status updates for a specific unique work by name.
     */
    fun getWorkInfoByUniqueName(workName: String): LiveData<List<WorkInfo>> {
        return workManager.getWorkInfosForUniqueWorkLiveData(workName)
    }

    /**
     * Cancels all background operations managed by this application.
     */
    fun cancelAllWork() {
        workManager.cancelAllWork()
    }
}
