package com.rey.spacenews.app.repository.entity

import java.time.ZonedDateTime


data class Article(
    val id: Int,
    val title: String,
    val url: String,
    val image: String,
    val site: String,
    val summary: String,
    val publishedDateTime: ZonedDateTime,
    val updatedDateTime: ZonedDateTime,
)
