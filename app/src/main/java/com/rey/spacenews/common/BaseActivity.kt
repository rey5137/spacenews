package com.rey.spacenews.common

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction


abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = FrameLayout(this)
        initRoute(savedInstanceState, view)
        setContent {
            contentView(view)
        }
        val route = entryRoute()
        if (!router.hasRootController() && route != null)
            router.setRoot(route)
    }

    open fun initRoute(savedInstanceState: Bundle?, viewGroup: ViewGroup) {
        router = Conductor.attachRouter(this, viewGroup, savedInstanceState)
    }

    abstract fun entryRoute(): RouterTransaction?

    override fun onBackPressed() {
        if (!router.handleBack())
            super.onBackPressed()
    }

    @Composable
    open fun contentView(conductorView: View) {
        MaterialTheme {
            AndroidView(factory = { conductorView })
        }
    }

}