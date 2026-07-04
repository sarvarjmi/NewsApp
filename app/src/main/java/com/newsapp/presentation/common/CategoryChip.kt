package com.newsapp.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = { onSelected(category) },
        label = {
            Text(
                text = category,
                style = MaterialTheme.typography.labelLarge
            )
        },
        modifier = modifier.padding(horizontal = 4.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            selectedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}
