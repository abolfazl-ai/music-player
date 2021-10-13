package com.example.compose.ui.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.ui.Screen
import com.example.compose.ui.composables.player_screen.PlayerScreen
import com.example.compose.ui.composables.player_screen.progress
import com.example.compose.ui.composables.screens.*
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

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

        BottomSheetScaffold(
            modifier = Modifier.padding(bottom = 56.dp),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopAppBar(title = { Text("Abolfazl is awesome :)") }) },
            scaffoldState = scaffoldState,
            sheetContent = {
                PlayerScreen(
                    modifier = Modifier.alpha(
                        2 * (sheetState.progress().coerceIn(0.5f, 1f) - 0.5f)
                    ),
                    progress = 1f
                )
            },
            sheetElevation = 3.dp,
            sheetPeekHeight = 56.dp
        ) {
            Box {
                SwipeRefresh(modifier = Modifier.alpha(backgroundAlpha),
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


        if (fabExpanded) Spacer(modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { fabExpanded = false; true })

        MotionLayout(
            ConstraintSet(
                """ {
                    fab: { 
                      bottom: ['parent', 'bottom', 16],
                      end: ['parent', 'end', 16],
                      custom: {
                        color: "#2962ff",
                        onColor: "#ffffff"
                      }
                    }
            } """
            ), ConstraintSet(
                """ {
                    fab: { 
                      top: ['parent', 'top', 244],
                      start: ['parent', 'start'],
                      end: ['parent', 'end'],
                      custom: {
                        color: "#ffffff",
                        onColor: "#2962ff"
                      }
                    }
            } """
            ),
            progress = sheetState.progress(),
            modifier = Modifier.fillMaxSize()
        ) {

            DraggableFab(
                Modifier
                    .layoutId("fab", "box")
                    .padding(horizontal = 16.dp, vertical = 132.dp),
                expanded = fabExpanded,
                onExpand = { fabExpanded = it },
                backgroundColor = motionProperties("fab").value.color("color"),
                contentColor = motionProperties("fab").value.color("onColor")
            )
        }
    }
}