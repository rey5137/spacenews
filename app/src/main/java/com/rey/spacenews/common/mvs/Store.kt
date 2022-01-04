package com.rey.spacenews.common.mvs

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class Store<C : Command, E : Event, S : State>(
    private val scope: CoroutineScope,
    context: CoroutineContext = Dispatchers.Default,
    initialState: S,
    reducer: Reducer<E, S>,
    initialEvents: Flow<E> = emptyFlow(),
    logger: StoreLogger<C, E, S>? = null
) {

    private val commandFlow = MutableSharedFlow<C>()
    private val stateFlow = flowOf(initialEvents, commandFlow.onEach { logger?.onCommand(this, it) }.flatMapMerge { processCommand(it) })
        .flattenMerge(2)
        .onEach { logger?.onEvent(this, it) }
        .scan(initialState, { state, event -> reducer.reduce(state, event) })
        .onEach { logger?.onState(this, it) }
        .flowOn(context)
        .stateIn(scope, SharingStarted.Eagerly, initialState)

    val states: StateFlow<S>
        get() = stateFlow

    protected open fun processCommand(command: C): Flow<E> = emptyFlow()

    fun dispatch(command: C, context: CoroutineContext = EmptyCoroutineContext) {
        scope.launch(context = context) { commandFlow.emit(command) }
    }
}

interface StoreLogger<C : Command, E : Event, S : State> {

    fun onCommand(store: Store<C, E, S>, command : C)

    fun onEvent(store: Store<C, E, S>, event : E)

    fun onState(store: Store<C, E, S>, state: S)

}
