package com.newsapp.core.performance

import android.content.Context
import com.newsapp.core.performance.PerformanceConfig
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okio.FileSystem
import okio.Path.Companion.toPath
import javax.inject.Singleton

/**
 * Hilt module to provide an optimized [ImageLoader] for Coil 3.
 * 
 * This configuration ensures that images are cached efficiently in memory and on disk,
 * and provides a smooth user experience with crossfade animations.
 */
@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader {
        return ImageLoader.Builder(context)
            // 1. Memory Cache: Keep recently used images in RAM for instant access.
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, PerformanceConfig.IMAGE_MEMORY_CACHE_PERCENT)
                    .build()
            }
            // 2. Disk Cache: Persist images on storage to avoid re-fetching from network.
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache").absolutePath.toPath())
                    .fileSystem(FileSystem.SYSTEM)
                    .maxSizePercent(0.02)
                    .build()
            }
            // 3. UX: Enable a subtle crossfade when an image finishes loading.
            .crossfade(true)
            .build()
    }
}
