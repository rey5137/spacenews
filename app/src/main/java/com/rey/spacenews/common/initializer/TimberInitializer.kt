package com.rey.spacenews.common.initializer

import android.app.Application
import com.rey.spacenews.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer() {

    override fun doInit(app: Application) {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

}