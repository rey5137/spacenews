package com.rey.spacenews.app.feature.home

import com.rey.spacenews.app.feature.home.contract.*
import com.rey.spacenews.app.repository.ContentRepository
import com.rey.spacenews.app.repository.entity.Content
import com.rey.spacenews.app.repository.entity.ContentType
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
    private val repository: ContentRepository,
    private val contentType: ContentType,
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
            is LoadMoreCommand -> command.process()
            is RefreshCommand -> command.process()
        }
    }

    private fun LoadMoreCommand.process(): Flow<HomeScreenEvent> = flow {
        val state = states.value
        if (!state.loading) {
            val page = state.page + 1
            emit(ContentLoadingEvent(page, false))
            emit(
                ContentLoadedEvent(
                    page,
                    false,
                    repository.getContents(type = contentType, page = page, size = PAGE_SIZE)
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    private fun RefreshCommand.process(): Flow<HomeScreenEvent> = flow {
        val state = states.value
        if (!state.isRefreshing) {
            emit(ContentLoadingEvent(0, true))
            emit(ContentLoadedEvent(0, true, repository.getContents(type = contentType, page = 0, size = PAGE_SIZE)))
        }
    }.flowOn(Dispatchers.IO)
}


class HomeReducer : Reducer<HomeScreenEvent, HomeScreenState> {

    override fun reduce(oldState: HomeScreenState, event: HomeScreenEvent): HomeScreenState {
        return when (event) {
            is ContentLoadingEvent -> event.reduce(oldState)
            is ContentLoadedEvent -> event.reduce(oldState)
        }
    }

    private fun ContentLoadingEvent.reduce(oldState: HomeScreenState): HomeScreenState = when {
        refreshing -> oldState.copy(isRefreshing = true)
        oldState.hasMore -> oldState.copy(
            loading = true,
            items = oldState.items.addLoading(page == 0)
        )
        else -> oldState
    }

    private fun ContentLoadedEvent.reduce(oldState: HomeScreenState): HomeScreenState = when {
        refreshing -> {
            val hasMore = contents.size == PAGE_SIZE
            oldState.copy(
                isRefreshing = false,
                page = 0,
                items = emptyList<Item>().addContents(contents, hasMore),
                contentIds = contents.map { it.id }.toSet(),
                hasMore = hasMore
            )
        }
        page == oldState.page + 1 -> {
            val hasMore = contents.size == PAGE_SIZE
            val ids = oldState.contentIds.toMutableSet()
            val contents = contents.filter { !ids.contains(it.id) }
                .onEach { ids.add(it.id) }
            oldState.copy(
                loading = false,
                page = page,
                items = oldState.items.addContents(contents, hasMore),
                contentIds = ids.toSet(),
                hasMore = hasMore,
            )
        }
        else -> oldState
    }

    private fun List<Item>.addLoading(firstTime: Boolean): List<Item> {
        val item = this.find { it is LoadingItem } as LoadingItem?
        return if (item == null || item.firstTime != firstTime) {
            if (firstTime)
                listOf(LoadingItem(true))
            else
                this.filterIsInstance<ContentItem>() + LoadingItem(false)
        } else
            this
    }

    private fun List<Item>.addContents(contents: List<Content>, addLoading: Boolean): List<Item> {
        val items = mutableListOf<Item>()
        items.addAll(this.filterIsInstance<ContentItem>())
        contents.map { ContentItem(it) }.forEach { items.add(it) }
        if (addLoading)
            items.add(LoadingItem(false))
        return items
    }

}