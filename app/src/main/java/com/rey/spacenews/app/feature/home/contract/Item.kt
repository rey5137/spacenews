package com.rey.spacenews.app.feature.home.contract

import com.rey.spacenews.app.repository.entity.Article

sealed interface Item

data class LoadingItem(val page: Int): Item {
    override fun toString() = "LoadingItem($page)"
}

data class ArticleItem(val article: Article): Item {
    override fun toString() = "ArticleItem(${article.id})"
}
