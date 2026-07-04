package com.newsapp.data.remote.mapper

import com.newsapp.data.remote.dto.SourceDto
import com.newsapp.domain.model.Source

/**
 * Maps [SourceDto] from the data layer to [Source] in the domain layer.
 * 
 * Mapping decisions:
 * - [id]: Preserved as nullable string.
 * - [name]: Provides a default value of "Unknown" if null to ensure UI reliability.
 */
fun SourceDto.toDomain(): Source {
    return Source(
        id = id,
        name = name ?: "Unknown"
    )
}
