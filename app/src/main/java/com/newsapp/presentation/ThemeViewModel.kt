package com.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.core.common.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to manage global theme state.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    /**
     * Exposes the current dark mode preference.
     * null = System, true = Dark, false = Light.
     */
    val isDarkMode: StateFlow<Boolean?> = themePreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    /**
     * Toggles between Light and Dark mode.
     * If current is null (System), it defaults to the opposite of system or just toggles.
     * For simplicity, this implementation toggles between true/false.
     */
    fun toggleTheme(currentIsDark: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkMode(!currentIsDark)
        }
    }
}
