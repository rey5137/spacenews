package com.rey.spacenews

import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bluelinelabs.conductor.asTransaction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.rey.spacenews.app.feature.home.HomeController
import com.rey.spacenews.common.BaseActivity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


@ExperimentalPagerApi
@ExperimentalMaterialApi
class MainActivity : BaseActivity() {

    private val drawerActionFlow = MutableSharedFlow<DrawerAction>()

    override fun entryRoute() = HomeController().asTransaction()

    @Composable
    override fun contentView(conductorView: View) {
        val scaffoldState = rememberScaffoldState()
        val drawerActionState = drawerActionFlow.collectAsState(initial = DrawerAction.NONE)
        val scope = rememberCoroutineScope()

        BackHandler(enabled = scaffoldState.drawerState.isOpen) {
            scope.launch {  scaffoldState.drawerState.close() }
        }
        MaterialTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                drawerContent = { Text(text = "drawerContent") },
                drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                content = { contentPadding ->
                    Box(modifier = Modifier.padding(contentPadding)) {
                        AndroidView(factory = { conductorView })
                    }
                }
            )
        }
        LaunchedEffect(drawerActionState.value) {
            when(drawerActionState.value) {
                is DrawerAction.Open -> scaffoldState.drawerState.open()
                else -> scaffoldState.drawerState.close()
            }
        }
    }

    suspend fun openDrawer() {
        drawerActionFlow.emit(DrawerAction.Open(System.currentTimeMillis()))
    }

    suspend fun closeDrawer() {
        drawerActionFlow.emit(DrawerAction.Close(System.currentTimeMillis()))
    }

    sealed interface DrawerAction {
        object NONE : DrawerAction
        data class Open(val time: Long) : DrawerAction
        data class Close(val time: Long) : DrawerAction
    }
}