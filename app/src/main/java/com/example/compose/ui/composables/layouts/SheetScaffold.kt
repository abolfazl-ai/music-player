package com.example.compose.ui.composables.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ui.composables.util_composables.Shadow
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.*
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SheetScaffold(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    appBar: @Composable () -> Unit,
    //Fab Attributes
    fab: @Composable () -> Unit,
    fabDismissShow: Boolean = false,
    fabDismiss: () -> Unit = {},
    //Bottom Nav Attributes
    bottomNav: @Composable () -> Unit,
    showBottomNav: Boolean = true,
    //Nav Drawer Attributes
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    drawerContent: @Composable ColumnScope.() -> Unit = {},
    drawerBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    //Stage Attributes
    stageContent: @Composable () -> Unit,
    stageBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    stageSheetState: SheetState = rememberSheetState(SheetValue.Collapsed/*, spring(1f, 750f)*/),
    //Queue Attributes
    queueContent: @Composable () -> Unit,
    queueBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    queueSheetState: SheetState = rememberSheetState(SheetValue.Collapsed),
    content: @Composable () -> Unit
) = BoxWithConstraints(modifier.fillMaxSize()) {

    val state by viewModel.mainScaffoldState.collectAsState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val realPeekHeight = getRealPeekHeight(density, state.showBottomNav, LocalWindowInsets.current.navigationBars.bottom)

    val stageAnchors = mapOf(-realPeekHeight.toFloat() to SheetValue.Collapsed, -constraints.maxHeight.toFloat() to SheetValue.Expanded)
    val stageSwipeable = Modifier.swipeable(stageSheetState, stageAnchors, Orientation.Vertical, resistance = null)

    val queueAnchors = getQueueAnchors(density, maxWidth, LocalWindowInsets.current.statusBars.top)
    val queueSwipeable = Modifier.swipeable(queueSheetState, queueAnchors, Orientation.Vertical)

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        ModalDrawer(
            drawerShape = RectangleShape, gesturesEnabled = stageSheetState.realProgress == 0f,
            drawerBackgroundColor = drawerBackgroundColor,
            drawerContent = drawerContent, drawerState = drawerState
        ) {
            BackHandler(
                (stageSheetState.realProgress > 0) ||
                        (queueSheetState.realProgress > 0) ||
                        state.isFabExpanded ||
                        drawerState.isOpen
            ) {
                scope.launch {
                    if (drawerState.isOpen) drawerState.close()
                    else if (state.isFabExpanded) viewModel.setFabState(false)
                    else if (queueSheetState.realProgress > 0) queueSheetState.collapse()
                    else if (stageSheetState.realProgress > 0)
                        stageSheetState.animateTo(SheetValue.Collapsed, spring(DampingRatioNoBouncy, StiffnessMediumLow))
                }
            }

            SheetScaffoldStack( width = maxWidth, showNav = state.showBottomNav, showDismiss = fabDismissShow,
                appBar = appBar, bottomNav = bottomNav, fab = fab,
                stageContent = {
                    Surface(
                        stageSwipeable.fillMaxSize().clickable(stageSheetState.isCollapsed) { scope.launch { stageSheetState.expand() } },
                        shadowElevation = SheetElevation, color = MaterialTheme.colorScheme.primaryContainer, content = stageContent
                    )
                },
                stagePeekHeight = realPeekHeight,
                stageSheetState = stageSheetState,
                queueContent = {
                    Surface(
                        modifier = queueSwipeable.alpha(stageSheetState.realProgress.compIn(0.9f)).fillMaxSize()
                            .padding(horizontal = QueueMargin),
                        shadowElevation = SheetElevation, color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(QueueRadius, QueueRadius, 0.dp, 0.dp),
                        content = queueContent
                    )
                },
                queueOffset = queueSheetState.offset.value.roundToInt(), body = content
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
private fun SheetScaffoldStack(
//    state: MainScaffoldState,
    showNav: Boolean,
    showDismiss: Boolean,
    width: Dp, appBar: @Composable () -> Unit,
    bottomNav: @Composable () -> Unit,
    fab: @Composable () -> Unit,
    stageContent: @Composable () -> Unit,
    stagePeekHeight: Int,
    stageSheetState: SheetState,
    queueContent: @Composable () -> Unit,
    queueOffset: Int,
    body: @Composable () -> Unit,
) {
    val navAnimator by animateFloatAsState(if (showNav) 1f else 0f)

    val fabRange = remember(width) { width + FabSize + FabMargin + StageSpacing + TimeLineHeight }
    val stageOffset = stageSheetState.offset.value.roundToInt()

    var transProgress by remember { mutableStateOf(0f) }

    with(LocalDensity.current) {
        LaunchedEffect(stageSheetState.realProgress) {
            launch {
                transProgress = (-(stageOffset.coerceIn(-stagePeekHeight - fabRange.roundToPx(), -stagePeekHeight) +
                        stagePeekHeight) / fabRange.toPx()).coerceIn(0f, 1f)
            }
        }
    }

    val fabShadowAnimator by animateFloatAsState(if (showDismiss) 0.5f else 0f)

    Layout(
        content = {
            body()
            Shadow(alpha = fabShadowAnimator, color = MaterialTheme.colorScheme.background)
            appBar()
            Shadow(alpha = stageSheetState.realProgress / 1.5f)
            stageContent()
            bottomNav()
            queueContent()
            Dismisser(showDismiss) { /*viewModel.setFabState(false)*/ }
            fab()
        }
    ) { m, c ->

        val appBarPlc = m[2].measure(c)
        val bodyPlc = m.first().measure(c.copy(minHeight = 0, maxHeight = c.maxHeight - appBarPlc.height - stagePeekHeight))
        val fabShadowPlc = m[1].measure(c)
        val sheetShadowPlc = m[3].measure(c)
        val stagePlc = m[4].measure(c.copy(minHeight = c.maxHeight, maxHeight = c.maxHeight))
        val botNavPlc = m[5].measure(c)
        val queuePlc = m[6].measure(c)
        val fabPlc = FabSize.roundToPx().let { m.last().measure(c.copy(it, it, it, it)) }

        with(c) {
            layout(maxWidth, maxHeight) {

                bodyPlc.place(0, (appBarPlc.height * (1 - stageSheetState.realProgress)).toInt())
                if (fabShadowAnimator > 0) fabShadowPlc.place(0, 0)
                appBarPlc.place(0, -(appBarPlc.height * stageSheetState.realProgress).toInt())
                if (stageSheetState.realProgress > 0) sheetShadowPlc.place(0, 0)
                stagePlc.place(0, maxHeight + stageOffset)
                botNavPlc.place(0, (maxHeight - navAnimator * botNavPlc.height * (1 - transProgress)).toInt())
                if (showDismiss) m[7].measure(c.copy(minWidth = c.maxWidth, minHeight = c.maxHeight)).place(0, 0)
                fabPlc.place(
                    (maxWidth - fabPlc.width) / 2 +
                            ((1 - transProgress) * (maxWidth / 2 - fabPlc.width / 2 - FabMargin.toPx())).toInt(),
                    maxHeight + stageOffset.coerceAtMost(-(stagePeekHeight + fabRange.roundToPx()))
                            + maxWidth + (StageSpacing + TimeLineHeight).roundToPx()
                )
                queuePlc.place(0, maxHeight + stageOffset + queueOffset)
            }
        }
    }
}

private fun getQueueAnchors(density: Density, width: Dp, statusBarHeight: Int) = with(density) {
    mapOf(
        (width + StageSpacing.times(2) + FabSize + TimeLineHeight).toPx() to SheetValue.Collapsed,
        statusBarHeight + QueueMargin.toPx() to SheetValue.Expanded
    )
}

private fun getRealPeekHeight(density: Density, showBottomNav: Boolean, navBarHeight: Int) = with(density) {
    (StagePeekHeight + BottomNavHeight.times(if (showBottomNav) 1f else 0f)).roundToPx() + navBarHeight
}

@Composable
private fun Dismisser(active: Boolean = false, onDismiss: () -> Unit) =
    Spacer(Modifier.pointerInput(active) {
        if (active) forEachGesture {
            awaitPointerEventScope {
                awaitFirstDown(requireUnconsumed = false)
                onDismiss()
            }
        }
    })