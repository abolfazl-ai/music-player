package com.example.compose.ui.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.compose.ui.composables.main_screens.*
import com.example.compose.ui.composables.player_screen.PlayerScreen
import com.example.compose.ui.screens
import com.example.compose.utils.kotlin_extensions.progress
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

    LaunchedEffect(sheetState.progress()) {
        viewModel.bottomSheetProgress.value = sheetState.progress()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

        BottomSheetScaffold(
            backgroundColor = MaterialTheme.colors.background,
            scaffoldState = scaffoldState,
            sheetContent = {
                Box {
                    PlayerScreen()
                    if (viewModel.bottomSheetProgress.value < 1)
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(
                                    1 - 5 * (viewModel.bottomSheetProgress.collectAsState().value
                                        .coerceIn(0.2f, 0.4f) - 0.2f)
                                )
                                .background(MaterialTheme.colors.surface),
                            elevation = 2.5.dp
                        ) {}
                }
            },
            sheetElevation = 2.5.dp,
            sheetPeekHeight = 160.dp
        ) {
            Box {
                SwipeRefresh(modifier = Modifier
                    .padding(top = 82.dp)
                    .alpha(backgroundAlpha),
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
            }
        }

        MotionLayout(
            ConstraintSet(
                """ {
                    fab: { 
                      bottom: ['parent', 'bottom', 176],
                      end: ['parent', 'end', 16],
                      custom: {
                        color: "#2962ff",
                        onColor: "#ffffff"
                      }
                    },
                    nav: { 
                      bottom: ['parent', 'bottom',48],
                      start: ['parent', 'start'],
                      end: ['parent', 'end'],
                    }
            } """
            ), ConstraintSet(
                """ {
                    fab: { 
                      top: ['parent', 'top', 376],
                      start: ['parent', 'start'],
                      end: ['parent', 'end'],
                      custom: {
                        color: "#ffffff",
                        onColor: "#2962ff"
                      }
                    },
                    nav: { 
                      top: ['parent', 'bottom',32],
                      start: ['parent', 'start'],
                      end: ['parent', 'end'],
                    }
            } """
            ),
            progress = 2 * (viewModel.bottomSheetProgress.collectAsState().value.coerceIn(
                0.5f,
                1f
            ) - 0.5f),
            modifier = Modifier.fillMaxSize()
        ) {

            BottomNavigation(
                Modifier.layoutId("nav", "box"),
                backgroundColor = MaterialTheme.colors.surface
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

            Box(Modifier.fillMaxSize()) {
                if (fabExpanded) Spacer(modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter { fabExpanded = false;true })
            }

            DraggableFab(
                Modifier.layoutId("fab", "box"),
                expanded = fabExpanded,
                onExpand = { fabExpanded = it },
                backgroundColor = motionProperties("fab").value.color("color"),
                contentColor = motionProperties("fab").value.color("onColor")
            )
        }
    }
}