package com.rey.spacenews.common.util

import android.os.Build
import android.os.Looper
import com.rey.spacenews.common.mvs.*
import timber.log.Timber

class TimberStoreLogger<C: Command, E: Event, S: State> : StoreLogger<C, E, S> {

    override fun onCommand(store: Store<C, E, S>, command: C) {
        log("Command", store, command, false)
    }

    override fun onEvent(store: Store<C, E, S>, event: E) {
        log("Event", store, event, true)
    }

    override fun onState(store: Store<C, E, S>, state: S) {
        log("State", store, state, false)
    }

    private fun log(type: String, store: Store<C, E, S>, value: Any, checkMainThread: Boolean) {
        val tag = store::class.java.simpleName
        if (checkMainThread) {
            val mainThread =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Looper.getMainLooper().isCurrentThread
                else Thread.currentThread() === Looper.getMainLooper().thread
            if (mainThread) {
                Timber.e("%s | %s | Receive %s on main thread", type, tag, value)
                throw ReceiveEventOnMainThreadException(value)
            }
        }

        Timber.d("%s | %s | %s | %s", type, tag, Thread.currentThread(), value)
    }

    class ReceiveEventOnMainThreadException(value: Any) : RuntimeException("Cannot process $value on main thread")

}