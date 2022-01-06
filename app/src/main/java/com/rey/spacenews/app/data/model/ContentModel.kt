package com.rey.spacenews.app.data.model

data class ContentModel(
    val id: Int,
    val title: String,
    val url: String,
    val imageUrl: String,
    val newsSite: String,
    val summary: String,
    val publishedAt: String,
    val updatedAt: String,
)