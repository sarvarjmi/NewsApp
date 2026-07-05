package com.newsapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * NewsApp Shape System.
 * 
 * Standardized corner radii for consistent Material 3 aesthetics.
 */
val Shapes = Shapes(
    /** 4dp - Used for tight UI elements like Category Chips or Checkboxes. */
    small = RoundedCornerShape(4.dp),
    /** 12dp - The primary corner radius for News Cards and Dialogs. */
    medium = RoundedCornerShape(12.dp),
    /** 16dp - Used for large containers like Bottom Sheets or persistent surfaces. */
    large = RoundedCornerShape(16.dp),
    /** 28dp - Specifically for the pill-shaped Search Bar and Buttons. */
    extraLarge = RoundedCornerShape(28.dp)
)
