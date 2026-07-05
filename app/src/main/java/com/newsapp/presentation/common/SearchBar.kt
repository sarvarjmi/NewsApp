package com.newsapp.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.newsapp.ui.theme.MaterialThemeSpacing
import com.newsapp.ui.theme.NewsAppTheme

/**
 * A highly reusable, stateless SearchBar component built with Material 3.
 *
 * This component provides a consistent search input experience across the app,
 * supporting keyboard actions, focus management, and accessibility out of the box.
 *
 * @param query The current text value of the search bar.
 * @param onQueryChange Callback triggered when the text changes.
 * @param onSearch Callback triggered when the search action is triggered from the keyboard.
 * @param onClear Callback triggered when the clear icon is tapped.
 * @param modifier Modifier for the search bar container.
 * @param focusRequester Optional [FocusRequester] to programmatically control focus (e.g., auto-focus on entry).
 * @param placeholder The hint text to display when the query is empty.
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    placeholder: String = "Search news..."
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = MaterialThemeSpacing.medium)
            .focusRequester(focusRequester)
            .semantics {
                contentDescription = "Search input field"
            },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null, // Visual decoration
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClear()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.semantics {
                        contentDescription = "Clear search text"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        shape = MaterialTheme.shapes.extraLarge, // Material 3 Pill shape
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            disabledIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    )
}

/**
 * --- Previews ---
 */

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun SearchBarPreview() {
    NewsAppTheme {
        SearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            onClear = {}
        )
    }
}

@Preview(showBackground = true, name = "With Text")
@Composable
private fun SearchBarWithTextPreview() {
    NewsAppTheme {
        SearchBar(
            query = "Android 15 News",
            onQueryChange = {},
            onSearch = {},
            onClear = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchBarDarkPreview() {
    NewsAppTheme {
        SearchBar(
            query = "Dark Mode Search",
            onQueryChange = {},
            onSearch = {},
            onClear = {}
        )
    }
}
