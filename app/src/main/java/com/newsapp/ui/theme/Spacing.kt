package com.newsapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Custom spacing tokens for the NewsApp design system.
 *
 * Adopts a 4dp-grid system to ensure consistent alignment and rhythm across 
 * different screen sizes and components.
 */
data class Spacing(
    /** 4dp - Used for tight groupings (e.g., text and icon in a chip). */
    val extraSmall: Dp = 4.dp,
    /** 8dp - Standard small padding for nested items. */
    val small: Dp = 8.dp,
    /** 16dp - The primary padding for screen edges and content blocks. */
    val medium: Dp = 16.dp,
    /** 24dp - Used for large sectional spacing. */
    val large: Dp = 24.dp,
    /** 32dp - Hero header or extra large separation. */
    val extraLarge: Dp = 32.dp,
    /** 64dp - Centered empty/error state top margin. */
    val extraExtraLarge: Dp = 64.dp
)

/**
 * CompositionLocal key for accessing [Spacing] throughout the Composable tree.
 */
val LocalSpacing = compositionLocalOf { Spacing() }

/**
 * Convenience accessor for [Spacing] tokens within Composables.
 *
 * Example:
 * ```
 * Modifier.padding(MaterialThemeSpacing.medium)
 * ```
 */
val MaterialThemeSpacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
