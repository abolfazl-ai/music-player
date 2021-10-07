package com.example.compose.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.PlayerService
import com.example.compose.ui.composables.DraggableFab
import com.example.compose.ui.composables.screens.*
import com.example.compose.ui.theme.ComposeTheme
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

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
                FeatureThatRequiresCameraPermission()
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

@ExperimentalAnimationGraphicsApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
private fun FeatureThatRequiresCameraPermission() =
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
            Main()
        }
    }

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalAnimationGraphicsApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun Main(viewModel: MainViewModel = viewModel()) {

    val navController = rememberNavController()
    var fabExpanded by remember { mutableStateOf(false) }
    val backgroundAlpha by animateFloatAsState(targetValue = if (fabExpanded) 0.75f else 0f)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

        BottomSheetScaffold(
            modifier = Modifier.padding(bottom = 56.dp),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopAppBar(title = { Text(text = navController.currentBackStackEntryAsState().value?.destination?.route.toString()) })
            },
            sheetContent = { Spacer(Modifier.fillMaxSize()) },
            sheetElevation = 3.dp,
            sheetPeekHeight = 56.dp
        )
        {
            Box {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing.collectAsState().value),
                    onRefresh = { viewModel.refresh() }) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = Screen.Home.route,
                    ) {
                        composable(Screen.Home.route) { HomeScreen() }
                        composable(Screen.Songs.route) { SongsScreen() }
                        composable(Screen.Folders.route) { FoldersScreen() }
                        composable(Screen.Artists.route) { ArtistsScreen() }
                        composable(Screen.Albums.route) { AlbumsScreen() }
                    }
                }

                if (backgroundAlpha > 0)
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInteropFilter { fabExpanded = false; true }
                            .alpha(backgroundAlpha)
                            .background(MaterialTheme.colors.background)
                    )
            }
        }

        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            screens.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }

        DraggableFab(
            Modifier.padding(horizontal = 16.dp, vertical = 132.dp),
            expanded = fabExpanded,
            onExpand = { fabExpanded = it })
    }
}