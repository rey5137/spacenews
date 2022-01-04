package com.rey.spacenews.common.initializer

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

open class KoinInitializer(vararg modules: Module) : Initializer<Unit> {

    private val modules: List<Module> = listOf(*modules)

    override fun create(context: Context) {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(context)
            modules(modules)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

}