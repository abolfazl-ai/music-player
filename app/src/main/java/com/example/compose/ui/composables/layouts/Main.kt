package com.example.compose.ui.composables.layouts

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.compose.ui.Libraries
import com.example.compose.ui.Page
import com.example.compose.ui.composables.app_bar.EmoAppBar
import com.example.compose.ui.composables.bottom_nav.EmoBottomNav
import com.example.compose.ui.composables.fab.EmoFab
import com.example.compose.ui.composables.library_screens.*
import com.example.compose.ui.composables.stage.Stage
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Main() {
    PermissionRequest(modifier = Modifier.fillMaxSize(),
        requestScreen = { RequestScreen(it) }) {
        Home2()
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Home(viewModel: MainViewModel = hiltViewModel()) {

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    SheetScaffold(
        stageContent = { Stage() },
        queueContent = {},
        bottomNav = { EmoBottomNav(Modifier.navigationBarsPadding(), Libraries, navController) { viewModel.setCurrentPage(it) } },
        appBar = { EmoAppBar(Modifier.statusBarsPadding()) },
        fab = { EmoFab() }
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.preferences.isScanning.collectAsState(initial = false).value),
            onRefresh = { scope.launch { viewModel.repository.scan() } }) {
            NavHost(navController = navController, startDestination = Page.Libraries.HomePage.route) {
                composable(Page.Libraries.HomePage.route) { HomeLibrary() }
                composable(Page.Libraries.Songs.route) { SongsLibrary() }
                navigation("folders", Page.Libraries.Folders.route) { foldersLibrary(viewModel, navController) }
                composable(Page.Libraries.Artists.route) { ArtistsLibrary() }
                composable(Page.Libraries.Albums.route) { AlbumsLibrary() }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun Home2(viewModel: MainViewModel = hiltViewModel()) {

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    SheetScaffold2(
        appBar = { EmoAppBar(Modifier.statusBarsPadding()) },
        fab = { EmoFab() },
        bottomNav = { EmoBottomNav(Modifier.navigationBarsPadding(), Libraries, navController) { viewModel.setCurrentPage(it) } },
        drawerContent = {},
        stageContent = { Stage() },
        queueContent = {},
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.preferences.isScanning.collectAsState(initial = false).value),
            onRefresh = { scope.launch { viewModel.repository.scan() } }) {
            NavHost(navController = navController, startDestination = Page.Libraries.HomePage.route) {
                composable(Page.Libraries.HomePage.route) { HomeLibrary() }
                composable(Page.Libraries.Songs.route) { SongsLibrary() }
                navigation("folders", Page.Libraries.Folders.route) { foldersLibrary(viewModel, navController) }
                composable(Page.Libraries.Artists.route) { ArtistsLibrary() }
                composable(Page.Libraries.Albums.route) { AlbumsLibrary() }
            }
        }
    }
}