package com.rey.spacenews.common

import com.rey.spacenews.common.TestCommand.DecreaseCommand
import com.rey.spacenews.common.TestCommand.IncreaseCommand
import com.rey.spacenews.common.TestEvent.AddEvent
import com.rey.spacenews.common.TestEvent.SetEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class StoreTest {

    private val job = Job()
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(job + testDispatcher)

    @ExperimentalCoroutinesApi
    @Test
    fun `test dispatch command`() = testScope.runBlockingTest {
        val store = TestStore(this, TestCoroutineDispatcher() + CoroutineName("storeTest"), emptyFlow())
        val expectedStates = mutableListOf(TestState(0), TestState(1), TestState(2), TestState(1))

        launch {
            store.states.collect { value ->
                Assert.assertEquals(expectedStates.removeAt(0), value)
            }
        }

        launch {
            store.dispatch(IncreaseCommand)
            store.dispatch(IncreaseCommand)
            store.dispatch(DecreaseCommand)
            delay(100)
            job.cancel()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test dispatch command with initial events`() = testScope.runBlockingTest {
        val store = TestStore(
            scope = this,
            context = TestCoroutineDispatcher() + CoroutineName("storeTest"),
            initialEvents = flowOf(SetEvent(10)).onEach {
                println("[${Thread.currentThread().name}] initial: $it")
            }.flowOn(TestCoroutineDispatcher() + CoroutineName("initialFlow"))
        )
        val expectedStates = mutableListOf(TestState(10), TestState(11), TestState(10))

        launch(TestCoroutineDispatcher() + CoroutineName("collectFlow")) {
            store.states.collect {
                Assert.assertEquals(expectedStates.removeAt(0), it)
            }
        }

        launch {
            store.dispatch(IncreaseCommand)
            store.dispatch(DecreaseCommand)
            delay(100)
            job.cancel()
        }
    }

}

sealed class TestCommand : Command {
    object IncreaseCommand : TestCommand()
    object DecreaseCommand : TestCommand()
}

sealed class TestEvent : Event {
    data class AddEvent(val value: Int) : TestEvent()
    data class SetEvent(val value: Int) : TestEvent()
}

data class TestState(val value: Int = 0) : State

class TestReducer : Reducer<TestEvent, TestState> {
    override fun reduce(oldState: TestState, event: TestEvent): TestState = when (event) {
        is AddEvent -> oldState.copy(value = oldState.value + event.value)
        is SetEvent -> oldState.copy(value = event.value)
    }
}

class TestStore(scope: CoroutineScope, context: CoroutineContext, initialEvents: Flow<TestEvent>) : Store<TestCommand, TestEvent, TestState>(
    scope = scope,
    context = context,
    initialState = TestState(),
    reducer = TestReducer(),
    initialEvents = initialEvents,
    logger = Logger()
) {

    override fun processCommand(command: TestCommand): Flow<AddEvent> = when (command) {
        IncreaseCommand -> flowOf(AddEvent(1))
        DecreaseCommand -> flowOf(AddEvent(-1))
    }

}

class Logger: StoreLogger<TestCommand, TestEvent, TestState> {
    override fun onCommand(command: TestCommand) {
        println("[${Thread.currentThread().name}] command: $command")
    }

    override fun onEvent(event: TestEvent) {
        println("[${Thread.currentThread().name}] event: $event")
    }

    override fun onState(state: TestState) {
        println("[${Thread.currentThread().name}] state: $state")
    }

}