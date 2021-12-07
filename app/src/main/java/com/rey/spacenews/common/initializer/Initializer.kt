package com.rey.spacenews.common.initializer

import android.app.Application

open class Initializer(private val children: List<Initializer> = listOf()) {

    internal open fun doInit(app: Application) {}

    fun init(app: Application) {
        doInit(app)
        children.forEach { it.init(app) }
    }

}