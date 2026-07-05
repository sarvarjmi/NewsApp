package com.newsapp.core.performance

/**
 * Global performance configurations for the NewsApp.
 */
object PerformanceConfig {
    /**
     * The standard page size for all paginated lists.
     */
    const val DEFAULT_PAGE_SIZE = 20

    /**
     * How many items before the end of the list to start prefetching the next page.
     */
    const val PREFETCH_DISTANCE = 5

    /**
     * Maximum size of the image memory cache as a percentage of total application memory.
     */
    const val IMAGE_MEMORY_CACHE_PERCENT = 0.25

    /**
     * Maximum size of the image disk cache in bytes (50MB).
     */
    const val IMAGE_DISK_CACHE_SIZE = 50 * 1024 * 1024L

    /**
     * Network cache size in bytes (10MB).
     */
    const val NETWORK_CACHE_SIZE = 10 * 1024 * 1024L
}
