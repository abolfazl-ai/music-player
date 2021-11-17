package com.example.compose.ui.composables.layouts

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.ui.Screen
import com.example.compose.ui.composables.DraggableFab
import com.example.compose.ui.composables.library_screens.*
import com.example.compose.ui.composables.player_screen.PlayerScreen
import com.example.compose.ui.screens
import com.example.compose.utils.resources.TAG
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalMaterial3Api
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun Main() {
    PermissionRequest(modifier = Modifier.fillMaxSize(),
        requestScreen = { RequestScreen(it) }) {
        Home()
    }
}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun Home(viewModel: MainViewModel = viewModel()) {

    val navController = rememberNavController()
    var fabExpanded by remember { mutableStateOf(false) }
    var fabDragging by remember { mutableStateOf(false) }
    val backgroundAlpha by animateFloatAsState(targetValue = if (fabExpanded) 0.3f else 1f)

    ModalDrawer(
        drawerShape = RectangleShape,
        gesturesEnabled = !fabDragging,
        drawerContent = { }) {
        SheetScaffold(
            playerContent = { PlayerScreen(progress = { it }) },
            queueContent = {},
            bottomNav = {
                Column {
                    BottomNavigation(
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = 12.dp
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        screens.forEach { screen ->
                            BottomNavigationItem(
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                icon = {
                                    androidx.compose.material.Icon(
                                        screen.icon,
                                        contentDescription = null
                                    )
                                },
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
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsHeight()
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            },
            appBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(82.dp),
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 26.dp)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Abolfazl is awesome",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            },
            fab = { progress ->
                var isPlaying by remember { mutableStateOf(false) }
                DraggableFab(
                    transProgress = { progress },
                    expanded = fabExpanded,
                    onExpand = { fabExpanded = it },
                    isPlaying = isPlaying,
                    onDrag = { fabDragging = it }
                ) { isPlaying = !isPlaying }
            }, showDismiss = fabExpanded, onDismiss = {
                Log.e(TAG, "Home: ")
                fabExpanded = false
            }
        ) {
            SwipeRefresh(modifier = Modifier
                .alpha(backgroundAlpha),
                state = rememberSwipeRefreshState(viewModel.isRefreshing.collectAsState().value),
                onRefresh = { viewModel.refresh() }) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                ) {
                    composable(Screen.Home.route) { HomeLibrary() }
                    composable(Screen.Songs.route) { SongsLibrary() }
                    composable(Screen.Folders.route) { FoldersLibrary() }
                    composable(Screen.Artists.route) { ArtistsLibrary() }
                    composable(Screen.Albums.route) { AlbumsLibrary() }
                }
            }
        }
    }
}