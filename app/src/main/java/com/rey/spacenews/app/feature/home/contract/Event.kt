package com.rey.spacenews.app.feature.home.contract

import com.rey.spacenews.app.repository.entity.Content
import com.rey.spacenews.common.mvs.Event

sealed interface HomeScreenEvent : Event

data class ContentLoadingEvent(val page: Int, val refreshing: Boolean): HomeScreenEvent

data class ContentLoadedEvent(val page: Int, val refreshing: Boolean, val contents: List<Content>): HomeScreenEvent {
    override fun toString() = "ContentLoadedEvent(page=$page, num=${contents.size})"
}