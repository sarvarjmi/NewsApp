package com.newsapp.core.background.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.newsapp.data.local.datasource.LocalNewsDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheCleanupWorkerTest {

    private lateinit var context: Context
    private val localDataSource: LocalNewsDataSource = mockk()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun cacheCleanupWorker_doWork_shouldCallDeleteOldArticlesAndReturnSuccess() {
        coEvery { localDataSource.deleteOldArticles(any()) } returns Unit

        val worker = TestListenableWorkerBuilder<CacheCleanupWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: androidx.work.WorkerParameters
                ): ListenableWorker? {
                    return CacheCleanupWorker(appContext, workerParameters, localDataSource)
                }
            })
            .build()

        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.success(), result)
            coVerify { localDataSource.deleteOldArticles(any()) }
        }
    }
}
