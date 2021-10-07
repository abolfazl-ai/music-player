package com.example.compose.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.PlayerService
import com.example.compose.ui.composables.DraggableFab
import com.example.compose.ui.composables.screens.SongsScreen
import com.example.compose.ui.theme.ComposeTheme
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    private val viewModel: MainViewModel by viewModels()

    private var playerService: PlayerService? = null

    @ExperimentalAnimationGraphicsApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        bindService()


        setContent {
            ComposeTheme {

                BottomSheetScaffold(
                    sheetContent = { Spacer(Modifier.fillMaxSize()) },
                    drawerGesturesEnabled = true,
                    drawerContent = {
                        Spacer(
                            Modifier
                                .fillMaxHeight()
                                .width(250.dp)
                        )
                    }, backgroundColor = MaterialTheme.colors.background,
                    sheetPeekHeight = 56.dp
                )
                {
                    Box(modifier = Modifier.padding(it)) {
                        SongsScreen(Modifier)
                        DraggableFab()
                    }
                    FeatureThatRequiresCameraPermission(Modifier.padding(it), viewModel)
                }
            }
        }
    }

    @ExperimentalMaterialApi
    private fun bindService() {
        if (this.applicationContext != null) {

            val i = Intent(this, PlayerService::class.java)
            bindService(i, viewModel.serviceController.serviceConnection, BIND_ABOVE_CLIENT)

            viewModel.serviceController.binder.observe(this) { binder ->
                playerService = binder?.getService()
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
private fun FeatureThatRequiresCameraPermission(modifier: Modifier, viewModel: MainViewModel) =
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

        val storagePermissionState =
            rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        PermissionRequired(
            permissionState = storagePermissionState,
            permissionNotGrantedContent = {
                if (doNotShowRationale) {
                    Text("Permission required")
                } else {
                    Column {
                        Text("The Storage Permission is important for this app. Please grant the permission.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = { storagePermissionState.launchPermissionRequest() }) {
                                Text("Ok!")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = { doNotShowRationale = true }) {
                                Text("Nope")
                            }
                        }
                    }
                }
            },
            permissionNotAvailableContent = {
                Column {
                    Text(
                        "Camera permission denied. See this FAQ with information about why we " +
                                "need this permission. Please, grant us access on the Settings screen."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {}) {
                        Text("Open Settings")
                    }
                }
            }
        ) {
            LaunchedEffect(0) { viewModel.repository.scan() }
        }
    }

@Composable
fun Main() {

}