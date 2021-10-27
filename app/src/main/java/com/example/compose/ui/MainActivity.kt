package com.example.compose.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compose.PlayerService
import com.example.compose.ui.composables.Main
import com.example.compose.ui.composables.SheetLayout
import com.example.compose.ui.theme.ComposeTheme
import com.example.compose.ui.theme.Purple700
import com.example.compose.ui.theme.Red700
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    private val viewModel: MainViewModel by viewModels()

    private var playerService: PlayerService? = null

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalAnimationGraphicsApi
    @ExperimentalPagerApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        bindService()
        setContent {
            ComposeTheme {
//                Main()
                SheetLayout(
                    playScreenContent = { Spacer(modifier = Modifier.background(Red700).fillMaxWidth().aspectRatio(1f))},
                    queueContent = {},
                    topBar = { TopAppBar(backgroundColor = Color.White) {Text("Abolfazl")} },
                    bottomAppBar = { Spacer(modifier = Modifier.background(Purple700).fillMaxWidth().height(56.dp)) }) {

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