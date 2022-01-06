package com.rey.spacenews

import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bluelinelabs.conductor.asTransaction
import com.rey.spacenews.app.feature.home.HomeController
import com.rey.spacenews.common.BaseActivity

class MainActivity : BaseActivity() {
    @ExperimentalMaterialApi
    override fun entryRoute() = HomeController().asTransaction()

    @Composable
    override fun contentView(conductorView: View) {
        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        elevation = 4.dp,
                        navigationIcon = {
                            IconButton(onClick = {/* Do Something*/ }) {
                                Icon(Icons.Filled.Menu, null)
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(id = R.string.app_name)
                            )
                        },
                    )
                },
                drawerContent = { Text(text = "drawerContent") },
                content = { contentPadding ->
                    Box(modifier = Modifier.padding(contentPadding)) {
                        AndroidView(factory = { conductorView })
                    }
                }
            )
        }
    }
}