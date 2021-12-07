package com.rey.spacenews.common.initializer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class KoinInitializer(vararg modules: Module) : Initializer() {

    private val modules: List<Module> = listOf(*modules)

    override fun doInit(app: Application) {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(app)
            modules(modules)
        }
    }

}