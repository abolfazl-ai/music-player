package com.example.compose.ui.composables.bottom_nav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.compose.ui.Page
import com.example.compose.ui.composables.util_composables.EmoIconButton
import com.example.compose.utils.resources.BottomNavElevation
import com.example.compose.utils.resources.BottomNavHeight
import com.example.compose.utils.resources.BottomNavRipple

@Composable
fun EmoBottomNav(
    modifier: Modifier = Modifier, pages: List<Page>, navController: NavController,
    color: Color = MaterialTheme.colorScheme.primary, contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    elevation: Dp = BottomNavElevation, onSelect: (Page) -> Unit
) = Surface(color = color, contentColor = contentColor, shadowElevation = elevation) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var currentPage by remember { mutableStateOf<Page>(Page.Libraries.HomePage) }

    LaunchedEffect(currentDestination) {
        pages.find { p -> currentDestination?.hierarchy?.any { it.route == p.route } == true }?.let { currentPage = it; onSelect(it) }
    }

    Row(
        modifier
            .fillMaxWidth()
            .height(BottomNavHeight),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        pages.forEach {
            EmoIconButton(rippleRadius = BottomNavRipple, onClick = {
                navController.navigate(it.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }) {
                Icon(
                    modifier = Modifier
                        .alpha(animateFloatAsState(if (it == currentPage) 1f else 0.6f).value)
                        .size(BottomNavHeight)
                        .padding(16.dp),
                    imageVector = it.icon, contentDescription = it.title
                )
            }
        }
    }
}