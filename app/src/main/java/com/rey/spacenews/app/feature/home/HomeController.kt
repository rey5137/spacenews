package com.rey.spacenews.app.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.bluelinelabs.conductor.Controller
import com.rey.spacenews.app.feature.home.contract.LoadMoreCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class HomeController : Controller(), KoinComponent {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val store = HomeStore(repository = get(), scope = scope, context = Dispatchers.Default)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val view = ComposeView(container.context).apply {
            setContent {
                homeScreen()
            }
        }
        scope.launch { store.dispatch(LoadMoreCommand) }
        return view
    }

    override fun onDestroy() {
        scope.cancel()
    }

    @Composable
    fun homeScreen() {
        Text(text = "Hello world")
    }


}