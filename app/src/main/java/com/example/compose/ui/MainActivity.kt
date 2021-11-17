package com.example.compose.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.compose.PlayerService
import com.example.compose.ui.composables.layouts.Main
import com.example.compose.ui.theme.AppTheme
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    private val viewModel: MainViewModel by viewModels()

    private var playerService: PlayerService? = null

    @ExperimentalMaterial3Api
    @ExperimentalPermissionsApi
    @ExperimentalFoundationApi
    @ExperimentalPagerApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationGraphicsApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        bindService()
        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Main()
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