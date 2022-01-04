package com.rey.spacenews.app.feature.home

import com.rey.spacenews.app.feature.home.contract.*
import com.rey.spacenews.app.repository.NewsRepository
import com.rey.spacenews.app.repository.entity.Article
import com.rey.spacenews.common.mvs.Reducer
import com.rey.spacenews.common.mvs.Store
import com.rey.spacenews.common.util.TimberStoreLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

private const val PAGE_SIZE = 10

class HomeStore(
    private val repository: NewsRepository,
    scope: CoroutineScope,
    context: CoroutineContext
) : Store<HomeScreenCommand, HomeScreenEvent, HomeScreenState>(
    scope = scope,
    context = context,
    initialState = HomeScreenState(),
    reducer = HomeReducer(),
    logger = TimberStoreLogger()
) {

    override fun processCommand(command: HomeScreenCommand): Flow<HomeScreenEvent> {
        return when (command) {
            is LoadMoreCommand -> flow {
                val state = states.value
                if (!state.loading) {
                    val page = state.page + 1
                    emit(ArticleLoadingEvent(page))
                    emit(ArticleLoadedEvent(page, repository.getArticles(page = page, PAGE_SIZE)))
                }
            }.flowOn(Dispatchers.IO)
        }
    }
}


class HomeReducer : Reducer<HomeScreenEvent, HomeScreenState> {

    override fun reduce(oldState: HomeScreenState, event: HomeScreenEvent): HomeScreenState {
        return when (event) {
            is ArticleLoadingEvent -> if (oldState.hasMore) oldState.copy(
                loading = true,
                items = oldState.items.addLoading(event.page == 0)
            ) else oldState
            is ArticleLoadedEvent -> {
                val hasMore = event.articles.size == PAGE_SIZE
                val ids = oldState.articleIds.toMutableSet()
                val articles = event.articles.filter { !ids.contains(it.id) }
                    .onEach { ids.add(it.id) }
                oldState.copy(
                    loading = false,
                    page = event.page,
                    items = oldState.items.addArticles(articles, hasMore),
                    articleIds = ids.toSet(),
                    hasMore = hasMore,
                )
            }
        }
    }

    private fun List<Item>.addLoading(firstTime: Boolean): List<Item> {
        val item = this.find { it is LoadingItem } as LoadingItem?
        return if (item == null || item.firstTime != firstTime) {
            if (firstTime)
                listOf(LoadingItem(true))
            else
                this.filterIsInstance<ArticleItem>() + LoadingItem(false)
        } else
            this
    }

    private fun List<Item>.addArticles(articles: List<Article>, addLoading: Boolean): List<Item> {
        val items = mutableListOf<Item>()
        items.addAll(this.filterIsInstance<ArticleItem>())
        articles.map { ArticleItem(it) }.forEach { items.add(it) }
        if (addLoading)
            items.add(LoadingItem(false))
        return items
    }

}