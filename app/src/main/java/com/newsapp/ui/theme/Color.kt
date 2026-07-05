package com.newsapp.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * NewsApp Color System (Material 3)
 *
 * This file defines the complete color palette for the application, following 
 * Material Design 3 (M3) specifications. Colors are organized by their roles
 * to ensure accessibility, consistency, and support for both Light and Dark themes.
 *
 * Color Roles Explanation:
 * - **Primary**: Used for the most frequent and important UI elements (e.g., brand, active states).
 * - **Secondary**: Used for less prominent UI elements (e.g., filter chips, selection controls).
 * - **Tertiary**: Used for accents and contrasting elements that balance the primary/secondary colors.
 * - **Neutral (Surface/Background)**: Used for containers and backdrops to provide structure without distraction.
 * - **Error**: Used to communicate critical states or failures.
 * - **Outline**: Used for boundaries and subtle separations (e.g., card borders, text field outlines).
 * - **Success/Warning/Info**: Semantic colors for non-critical status updates.
 */

// --- Light Theme Palette ---
val LightPrimary = Color(0xFF0061A4)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFD1E4FF)
val LightOnPrimaryContainer = Color(0xFF001D36)

val LightSecondary = Color(0xFF535F70)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFD7E3F7)
val LightOnSecondaryContainer = Color(0xFF101C2B)

val LightTertiary = Color(0xFF6B5778)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFF2DAFF)
val LightOnTertiaryContainer = Color(0xFF251431)

val LightError = Color(0xFFBA1A1A)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)

val LightBackground = Color(0xFFFDFCFF)
val LightOnBackground = Color(0xFF1A1C1E)
val LightSurface = Color(0xFFFDFCFF)
val LightOnSurface = Color(0xFF1A1C1E)
val LightSurfaceVariant = Color(0xFFDFE2EB)
val LightOnSurfaceVariant = Color(0xFF43474E)
val LightOutline = Color(0xFF73777F)
val LightOutlineVariant = Color(0xFFC3C7CF)

// --- Dark Theme Palette ---
val DarkPrimary = Color(0xFF9ECAFF)
val DarkOnPrimary = Color(0xFF003258)
val DarkPrimaryContainer = Color(0xFF00497D)
val DarkOnPrimaryContainer = Color(0xFFD1E4FF)

val DarkSecondary = Color(0xFFBBC7DB)
val DarkOnSecondary = Color(0xFF253140)
val DarkSecondaryContainer = Color(0xFF3B4858)
val DarkOnSecondaryContainer = Color(0xFFD7E3F7)

val DarkTertiary = Color(0xFFD7BEE4)
val DarkOnTertiary = Color(0xFF3B2948)
val DarkTertiaryContainer = Color(0xFF523F5F)
val DarkOnTertiaryContainer = Color(0xFFF2DAFF)

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

val DarkBackground = Color(0xFF1A1C1E)
val DarkOnBackground = Color(0xFFE2E2E6)
val DarkSurface = Color(0xFF1A1C1E)
val DarkOnSurface = Color(0xFFE2E2E6)
val DarkSurfaceVariant = Color(0xFF43474E)
val DarkOnSurfaceVariant = Color(0xFFC3C7CF)
val DarkOutline = Color(0xFF8D9199)
val DarkOutlineVariant = Color(0xFF43474E)

// --- Semantic / Functional Colors ---
// Success
val LightSuccess = Color(0xFF006D39)
val DarkSuccess = Color(0xFF81D995)

// Warning
val LightWarning = Color(0xFF8E4D00)
val DarkWarning = Color(0xFFFFB85E)

// Info
val LightInfo = Color(0xFF006495)
val DarkInfo = Color(0xFF8DCDFF)

// --- Utility Colors ---
/**
 * Shimmer base and highlight colors for loading skeletons.
 * These are designed to blend with the background of each theme.
 */
val LightShimmerBase = Color(0xFFE1E2E8)
val LightShimmerHighlight = Color(0xFFF0F1F7)
val DarkShimmerBase = Color(0xFF303030)
val DarkShimmerHighlight = Color(0xFF3D3D3D)
