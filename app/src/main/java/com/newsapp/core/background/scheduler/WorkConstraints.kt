package com.newsapp.core.background.scheduler

import androidx.work.Constraints
import androidx.work.NetworkType

/**
 * Centralized WorkManager constraints for the application.
 *
 * Defines reusable constraint sets that ensure background work only executes
 * when specific device conditions are met, such as network availability,
 * battery level, and storage space.
 */
object WorkConstraints {
    
    /**
     * Default constraints for standard background synchronization (e.g., News Refresh).
     * 
     * Conditions:
     * - Network: Connected (any)
     * - Battery: Not low
     * - Storage: Not low
     */
    val default: Constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .setRequiresStorageNotLow(true)
        .build()

    /**
     * Constraints for heavier maintenance tasks (e.g., Cache Cleanup).
     * 
     * Conditions:
     * - Device: Idle (to avoid competing with user interactions)
     * - Power: Charging (to preserve battery during long operations)
     */
    val maintenance: Constraints = Constraints.Builder()
        .setRequiresDeviceIdle(true)
        .setRequiresCharging(true)
        .build()
}
