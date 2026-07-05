package com.newsapp.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScrollBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    /**
     * Measures jank during scrolling.
     * Tip: Use the Android Studio Profiler during this test to check for excessive allocations.
     */
    @Test
    fun scrollCompilationNone() = scroll(CompilationMode.None())

    @Test
    fun scrollCompilationBaselineProfile() = scroll(CompilationMode.Partial())

    private fun scroll(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.newsapp",
        metrics = listOf(FrameTimingMetric()),
        compilationMode = compilationMode,
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
        
        device.waitForIdle()
        val list = device.findObject(By.res("news_list"))
        if (list != null) {
            list.setGestureMargin(device.displayWidth / 5)
            list.fling(Direction.DOWN)
            device.waitForIdle()
        }
    }
}
