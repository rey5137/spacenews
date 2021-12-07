package com.rey.spacenews

import com.bluelinelabs.conductor.asTransaction
import com.rey.spacenews.app.feature.home.HomeController
import com.rey.spacenews.common.BaseActivity

class MainActivity : BaseActivity() {
    override fun entryRoute() = HomeController().asTransaction()
}