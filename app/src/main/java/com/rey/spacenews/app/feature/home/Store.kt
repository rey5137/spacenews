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

    private val pageSize = 30

    override fun processCommand(command: HomeScreenCommand): Flow<HomeScreenEvent> {
        return when(command) {
            is LoadMoreCommand -> flow {
                val state = states.value
                if(!state.loading) {
                    val page = state.page + 1
                    emit(ArticleLoadingEvent(page))
                    emit(ArticleLoadedEvent(page, repository.getArticles(page = page, pageSize)))
                }
            }.flowOn(Dispatchers.IO)
        }
    }
}


class HomeReducer : Reducer<HomeScreenEvent, HomeScreenState> {

    override fun reduce(oldState: HomeScreenState, event: HomeScreenEvent): HomeScreenState {
        return when(event) {
            is ArticleLoadingEvent -> oldState.copy(loading = true, items = oldState.items.addLoading(event.page))
            is ArticleLoadedEvent -> oldState.copy(loading = false, items = oldState.items.addArticles(event.articles))
        }
    }

    private fun List<Item>.addLoading(page: Int) : List<Item> {
        if(page == 0)
            return listOf(LoadingItem(page))
        return this.filterIsInstance<ArticleItem>() + LoadingItem(page)
    }

    private fun List<Item>.addArticles(articles: List<Article>) : List<Item> {
        val items = this.filterIsInstance<ArticleItem>().toMutableList()
        articles.map { ArticleItem(it) }.forEach { items.add(it) }
        return items
    }

}