package com.rey.spacenews.app.feature.home.contract

import com.rey.spacenews.app.repository.entity.Article
import com.rey.spacenews.common.mvs.Event

sealed interface HomeScreenEvent : Event

data class ArticleLoadingEvent(val page: Int): HomeScreenEvent

data class ArticleLoadedEvent(val page: Int, val articles: List<Article>): HomeScreenEvent {
    override fun toString() = "ArticleLoadedEvent(page=$page, num=${articles.size})"
}