package com.rey.spacenews.app.feature.home.contract

import com.rey.spacenews.app.repository.entity.Article

sealed interface Item {
    val id: String
}

data class LoadingItem(val firstTime: Boolean): Item {
    override val id = "LoadingItem($firstTime)"

    override fun toString() = "LoadingItem($firstTime)"
}

data class ArticleItem(val article: Article): Item {
    override val id = "ArticleItem(${article.id})"
    override fun toString() = "ArticleItem(${article.id})"
}
