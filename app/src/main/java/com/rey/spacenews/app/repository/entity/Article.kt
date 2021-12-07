package com.rey.spacenews.app.repository.entity

import org.joda.time.LocalDateTime

data class Article(
    val id: Int,
    val title: String,
    val url: String,
    val image: String,
    val site: String,
    val summary: String,
    val publishedDateTime: LocalDateTime,
    val updatedDateTime: LocalDateTime,
)
