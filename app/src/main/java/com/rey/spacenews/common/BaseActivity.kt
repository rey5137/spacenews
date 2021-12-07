package com.rey.spacenews.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.rey.spacenews.R


abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var router: Router

    open val layoutResource = R.layout.base_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        initRoute(savedInstanceState)
        val route = entryRoute()
        if (!router.hasRootController() && route != null)
            router.setRoot(route)
    }

    open fun initRoute(savedInstanceState: Bundle?) {
        router = Conductor.attachRouter(this, findViewById(R.id.containerLayout), savedInstanceState)
    }

    abstract fun entryRoute(): RouterTransaction?

    override fun onBackPressed() {
        if (!router.handleBack())
            super.onBackPressed()
    }

}