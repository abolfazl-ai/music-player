package com.example.compose.ui.composables.layouts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun Main() {
    PermissionRequest(modifier = Modifier.fillMaxSize(),
        requestScreen = { RequestScreen(it) }) {
        Home()
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun Home(viewModel: MainViewModel = hiltViewModel()) {

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    var fabProgress by remember { mutableStateOf(0f) }
    var isMenuOpen by remember { mutableStateOf(false) }

    NeoScaffold(
        onSheetStateChange = { progress, fab -> viewModel.setSheetState(fab, progress); fabProgress = fab },
        appBar = { EmoAppBar(Modifier.statusBarsPadding()) },
        fab = { EmoFab(fabProgress, isMenuOpen, true) { isMenuOpen = !isMenuOpen } },
        showFabDismisser = isMenuOpen, onFabDismiss = { isMenuOpen = false },
        bottomNav = { EmoBottomNav(Modifier.navigationBarsPadding(), Libraries, navController) {  } },
        drawerContent = {},
        stageContent = { Stage() },
        queueContent = {}
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.preferences.isScanning.collectAsState(false).value),
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