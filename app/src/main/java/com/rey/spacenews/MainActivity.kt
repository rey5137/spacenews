package com.rey.spacenews

import androidx.compose.material.ExperimentalMaterialApi
import com.bluelinelabs.conductor.asTransaction
import com.rey.spacenews.app.feature.home.HomeController
import com.rey.spacenews.common.BaseActivity

class MainActivity : BaseActivity() {
    @ExperimentalMaterialApi
    override fun entryRoute() = HomeController().asTransaction()
}