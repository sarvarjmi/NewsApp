package com.newsapp.core.background

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.newsapp.core.background.worker.RefreshNewsWorker
import com.newsapp.domain.usecase.news.RefreshNewsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkManagerTest {
    private lateinit var context: Context
    private val refreshNewsUseCase: RefreshNewsUseCase = mockk()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun refreshNewsWorker_doWork_returnsSuccess() {
        coEvery { refreshNewsUseCase() } returns Unit

        val worker = TestListenableWorkerBuilder<RefreshNewsWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: androidx.work.WorkerParameters
                ): ListenableWorker? {
                    return RefreshNewsWorker(appContext, workerParameters, refreshNewsUseCase)
                }
            })
            .build()

        runBlocking {
            val result = worker.doWork()
            assertEquals(ListenableWorker.Result.success(), result)
        }
    }
}
