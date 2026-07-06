package com.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity to store pagination keys for the remote mediator.
 */
@Entity(tableName = "news_remote_keys")
data class NewsRemoteKeysEntity(
    @PrimaryKey
    val articleUrl: String,
    val prevPage: Int?,
    val nextPage: Int?
)
