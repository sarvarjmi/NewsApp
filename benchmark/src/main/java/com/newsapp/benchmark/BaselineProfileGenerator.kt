package com.newsapp.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class generates a baseline profile for the target package.
 * 
 * It covers:
 * 1. App Startup (Cold)
 * 2. Headlines Scrolling (LazyColumn performance)
 * 3. Search Interaction (Navigation and Input performance)
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = "com.newsapp",
        includeInStartupProfile = true
    ) {
        // CUJ 1: App Startup
        pressHome()
        startActivityAndWait()
        
        // CUJ 2: Headlines Scrolling
        device.waitForIdle()
        val list = device.findObject(By.res("news_list"))
        if (list != null) {
            list.setGestureMargin(device.displayWidth / 5)
            list.fling(Direction.DOWN)
            device.waitForIdle()
            
            // CUJ 3: Navigation to Detail
            val firstArticle = list.children[0]
            firstArticle.click()
            device.waitForIdle()
            device.pressBack()
            device.waitForIdle()
        }

        // CUJ 4: Search Interaction
        val searchButton = device.findObject(By.desc("Search News"))
        if (searchButton != null) {
            searchButton.click()
            device.waitForIdle()
            
            val searchInput = device.wait(Until.findObject(By.res("search_input")), 2000)
            if (searchInput != null) {
                searchInput.text = "Android"
                device.waitForIdle()
                
                // Scroll search results
                val searchList = device.findObject(By.res("news_list"))
                searchList?.fling(Direction.DOWN)
                device.waitForIdle()
            }
            
            // Go back
            device.pressBack()
        }
    }
}
