package com.rey.spacenews.app.feature.home.contract

import com.rey.spacenews.app.repository.entity.Content

sealed interface Item {
    val id: String
}

data class LoadingItem(val firstTime: Boolean): Item {
    override val id = "LoadingItem($firstTime)"
    override fun toString() = "LoadingItem($firstTime)"
}

data class ContentItem(val content: Content): Item {
    override val id = "ContentItem(${content.id})"
    override fun toString() = "ContentItem(${content.id})"
}
