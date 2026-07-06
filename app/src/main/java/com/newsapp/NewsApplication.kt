package com.newsapp

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.newsapp.core.background.scheduler.WorkScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NewsApplication : Application(), SingletonImageLoader.Factory, Configuration.Provider {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workScheduler: WorkScheduler

    override fun onCreate() {
        super.onCreate()
        
        // Initialize background synchronization
        workScheduler.schedulePeriodicNewsRefresh()
        workScheduler.schedulePeriodicAnalyticsSync()
        workScheduler.schedulePeriodicNotificationCheck()
        workScheduler.schedulePeriodicCacheCleanup()
    }

    override fun newImageLoader(context: Context): ImageLoader = imageLoader

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
