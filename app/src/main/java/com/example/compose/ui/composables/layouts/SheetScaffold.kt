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
import com.example.compose.viewmodel.MainScaffoldState
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun SheetScaffold(
    modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel(),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    appBar: @Composable () -> Unit, bottomNav: @Composable () -> Unit, fab: @Composable () -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit = {}, drawerBackground: Color = MaterialTheme.colorScheme.primaryContainer,
    stageContent: @Composable () -> Unit, stageBackground: Color = MaterialTheme.colorScheme.primaryContainer,
    stageSheetState: SheetState = rememberSheetState(SheetValue.Collapsed, spring(1f, 750f)),
    queueContent: @Composable () -> Unit, queueBackground: Color = MaterialTheme.colorScheme.surface,
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

    SheetScaffoldStack(
        viewModel = viewModel, state = state, width = maxWidth,
        appBar = appBar, bottomNav = bottomNav, fab = fab,
        drawerContent = drawerContent, drawerBackground = drawerBackground,
        stageContent = {
            Surface(stageSwipeable.fillMaxSize(), shadowElevation = SheetElevation, color = stageBackground) {
                stageContent()
                if (stageSheetState.isCollapsed) Spacer(modifier = Modifier.clickable { scope.launch { stageSheetState.expand() } })
            }
        },
        stagePeekHeight = realPeekHeight, stageSheetState = stageSheetState,
        queueContent = {
            Surface(
                modifier = queueSwipeable
                    .alpha(stageSheetState.myProgress.compIn(0.8f))
                    .fillMaxSize()
                    .padding(horizontal = QueueMargin),
                shadowElevation = SheetElevation, color = queueBackground,
                shape = RoundedCornerShape(QueueRadius, QueueRadius, 0.dp, 0.dp),
                content = queueContent
            )
        },
        queueSheetState = queueSheetState,
    ) { Surface(color = backgroundColor, content = content) }
}


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
private fun SheetScaffoldStack(
    viewModel: MainViewModel, state: MainScaffoldState, width: Dp,
    appBar: @Composable () -> Unit, bottomNav: @Composable () -> Unit, fab: @Composable () -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit, drawerBackground: Color,
    stageContent: @Composable () -> Unit, stagePeekHeight: Int, stageSheetState: SheetState,
    queueContent: @Composable () -> Unit, queueSheetState: SheetState,
    body: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    var showNav by remember { mutableStateOf(state.showBottomNav) }
    val navAnimator by animateFloatAsState(if (state.showBottomNav) 1f else 0f) { showNav = state.showBottomNav }

    val fabRange = remember(width) { width + FabSize + FabMargin + StageSpacing + TimeLineHeight }

    with(LocalDensity.current) {
        LaunchedEffect(stageSheetState.myProgress) {
            if (showNav == state.showBottomNav) {
                launch {
                    val transferProgress = (-(stageSheetState.offset.value.roundToInt()
                        .coerceIn(-stagePeekHeight - fabRange.roundToPx(), -stagePeekHeight) +
                            stagePeekHeight) / fabRange.toPx()).coerceIn(0f, 1f)

                    viewModel.setSheetState(transferProgress, stageSheetState.myProgress)
                }
            }
        }
    }

    val fabShadowAnimator by animateFloatAsState(if (state.isFabExpanded) 0.5f else 0f)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalDrawer(
        drawerShape = RectangleShape, gesturesEnabled = !state.isFabDragging && stageSheetState.myProgress == 0f,
        drawerBackgroundColor = drawerBackground, drawerContent = drawerContent, drawerState = drawerState
    ) {
        BackHandler(
            (stageSheetState.myProgress > 0) ||
                    (queueSheetState.myProgress > 0) ||
                    state.isFabExpanded ||
                    drawerState.isOpen
        ) {
            scope.launch {
                if (drawerState.isOpen) drawerState.close()
                else if (state.isFabExpanded) viewModel.setFabState(false)
                else if (queueSheetState.myProgress > 0) queueSheetState.collapse()
                else if (stageSheetState.myProgress > 0)
                    stageSheetState.animateTo(SheetValue.Collapsed, spring(DampingRatioNoBouncy, StiffnessMediumLow))
            }
        }

        Layout(
            content = {
                body()
                Shadow(alpha = fabShadowAnimator, color = MaterialTheme.colorScheme.background)
                appBar()
                Shadow(alpha = state.progress / 1.5f)
                stageContent()
                bottomNav()
                queueContent()
                Dismiss(state.isFabExpanded) { viewModel.setFabState(false) }
                fab()
            }
        ) { m, c ->

            val stageOffset = stageSheetState.offset.value.roundToInt()
            val queueOffset = queueSheetState.offset.value.roundToInt()

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

                    bodyPlc.place(0, (appBarPlc.height * (1 - state.progress)).toInt())
                    if (fabShadowAnimator > 0) fabShadowPlc.place(0, 0)
                    appBarPlc.place(0, -(appBarPlc.height * state.progress).toInt())
                    if (state.progress > 0) sheetShadowPlc.place(0, 0)
                    stagePlc.place(0, maxHeight + stageOffset)
                    botNavPlc.place(0, (maxHeight - navAnimator * botNavPlc.height * (1 - state.transProgress)).toInt())
                    if (state.isFabExpanded) m[7].measure(c.copy(minWidth = c.maxWidth, minHeight = c.maxHeight)).place(0, 0)
                    fabPlc.place(
                        (maxWidth - fabPlc.width) / 2 +
                                ((1 - state.transProgress) * (maxWidth / 2 - fabPlc.width / 2 - FabMargin.toPx())).toInt(),
                        maxHeight + stageOffset.coerceAtMost(-(stagePeekHeight + fabRange.roundToPx()))
                                + maxWidth + (StageSpacing + TimeLineHeight).roundToPx()
                    )
                    queuePlc.place(0, maxHeight + stageOffset + queueOffset)
                }
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
private fun Dismiss(active: Boolean = false, onDismiss: () -> Unit) =
    Spacer(Modifier.pointerInput(active) {
        if (active) forEachGesture {
            awaitPointerEventScope {
                awaitFirstDown(requireUnconsumed = false)
                onDismiss()
            }
        }
    })