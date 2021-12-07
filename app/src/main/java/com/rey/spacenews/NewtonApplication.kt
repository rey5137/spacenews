package com.rey.spacenews

import android.app.Application
import com.rey.spacenews.common.initializer.Initializer
import com.rey.spacenews.common.initializer.KoinInitializer
import com.rey.spacenews.common.initializer.TimberInitializer

class NewtonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Initializer(
            listOf(
                TimberInitializer(),
                KoinInitializer(
                    KoinModule.app,
                )
            )
        ).init(this)
    }
}