package com.example.compose.ui.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.ui.Screen
import com.example.compose.ui.composables.main_screens.*
import com.example.compose.ui.composables.player_screen.PlayerScreen
import com.example.compose.ui.screens
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalPermissionsApi
@ExperimentalPagerApi
@ExperimentalAnimationGraphicsApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun Main() {
    PermissionRequest(modifier = Modifier.fillMaxSize(),
        requestScreen = { RequestScreen(it) }) {
        Home()
    }
}

@ExperimentalPagerApi
@ExperimentalAnimationGraphicsApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun Home(viewModel: MainViewModel = viewModel()) {

    val navController = rememberNavController()
    var fabExpanded by remember { mutableStateOf(false) }
    val backgroundAlpha by animateFloatAsState(targetValue = if (fabExpanded) 0.3f else 1f)

    MainLayout(
        Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        sheetPeekHeight = 160.dp,
        playerScreen = { PlayerScreen() },
        miniPlayer = { Text(modifier = Modifier.alpha(it), text = "Mini Player") },
        bottomNav = {
            BottomNavigation(
                Modifier.padding(bottom = 48.dp),
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            ) {
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
        },
        appBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(82.dp),
                elevation = 4.dp,
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
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        },
        fab = { state ->
            var isPlaying by remember { mutableStateOf(false) }
            DraggableFab(
                state = state,
                expanded = fabExpanded,
                onExpand = { fabExpanded = it },
                isPlaying = isPlaying
            )
            { isPlaying = !isPlaying }
        },
        showDismissView = fabExpanded,
        onDismiss = { fabExpanded = false }
    ) {
        SwipeRefresh(modifier = Modifier
            .padding(it)
            .alpha(backgroundAlpha),
            state = rememberSwipeRefreshState(viewModel.isRefreshing.collectAsState().value),
            onRefresh = { viewModel.refresh() }) {
            NavHost(
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
    }
}