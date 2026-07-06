package com.newsapp.core.startup

import android.content.Context
import androidx.startup.Initializer
import com.newsapp.BuildConfig
import timber.log.Timber

/**
 * Automatically initializes Timber using AndroidX Startup library.
 * This keeps the [Application.onCreate] clean and allows for parallel initialization.
 */
@Suppress("EnsureInitializerMetadata")
class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
