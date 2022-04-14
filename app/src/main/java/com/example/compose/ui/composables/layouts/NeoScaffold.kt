package com.example.compose.ui.composables.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.util_composables.Shadow
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.*
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private object LayoutId {
    const val AppBar = 0
    const val BottomNav = 1
    const val Fab = 2
    const val FabShadow = 3
    const val FabDismisser = 4
    const val Stage = 5
    const val StageShadow = 6
    const val Queue = 7
    const val Body = 8
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NeoScaffold(
    modifier: Modifier = Modifier,
    onSheetStateChange: (progress: Float, fabProgress: Float) -> Unit = { _, _ -> },
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    //AppBar Attributes
    appBar: @Composable BoxScope.() -> Unit,
    showAppBar: Boolean = true,
    //Fab Attributes
    fab: @Composable BoxScope.() -> Unit,
    showFabDismisser: Boolean = false,
    onFabDismiss: () -> Unit = {},
    //Bottom Nav Attributes
    bottomNav: @Composable BoxScope.() -> Unit,
    showBottomNav: Boolean = true,
    //Nav Drawer Attributes
    drawerContent: @Composable ColumnScope.() -> Unit,
    drawerBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    //Stage Attributes
    stageContent: @Composable () -> Unit,
    stageBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    stageSheetState: SheetState = rememberSheetState(SheetValue.Collapsed/*, spring(1f, 750f)*/),
    //Queue Attributes
    queueContent: @Composable () -> Unit,
    queueBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    queueSheetState: SheetState = rememberSheetState(SheetValue.Collapsed),
    content: @Composable BoxScope.() -> Unit
) = BoxWithConstraints(modifier.fillMaxSize()) {

    val scope = rememberCoroutineScope()

    val fabShadowAnimator by animateFloatAsState(if (showFabDismisser) 0.5f else 0f)

    val peekHeight = getRealPeekHeight(LocalDensity.current, showBottomNav, LocalWindowInsets.current.navigationBars.bottom)
    val fabRange = remember(maxWidth) { maxWidth + FabSize + FabMargin + StageSpacing + TimeLineHeight }

    val stageOffset = stageSheetState.offset.value.roundToInt()
    val stageAnchors = mapOf(-peekHeight.toFloat() to SheetValue.Collapsed, -constraints.maxHeight.toFloat() to SheetValue.Expanded)
    val stageSwipeable = Modifier.swipeable(stageSheetState, stageAnchors, Orientation.Vertical, resistance = null)

    val queueAnchors = getQueueAnchors(LocalDensity.current, maxWidth, LocalWindowInsets.current.statusBars.top)
    val queueSwipeable = Modifier.swipeable(queueSheetState, queueAnchors, Orientation.Vertical)

    var transProgress by remember { mutableStateOf(0f) }

    with(LocalDensity.current) {
        LaunchedEffect(stageSheetState.realProgress) {
            launch {
                transProgress = (-(stageOffset.coerceIn(-peekHeight - fabRange.roundToPx(), -peekHeight) +
                        peekHeight) / fabRange.toPx()).coerceIn(0f, 1f)
                onSheetStateChange(stageSheetState.realProgress, transProgress)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        ModalDrawer(
            drawerShape = RectangleShape, gesturesEnabled = stageSheetState.realProgress == 0f,
            drawerBackgroundColor = drawerBackgroundColor,
            drawerContent = drawerContent, drawerState = drawerState
        ) {
            BackHandler(
                (stageSheetState.realProgress > 0) ||
                        (queueSheetState.realProgress > 0) ||
                        showFabDismisser || drawerState.isOpen
            ) {
                scope.launch {
                    if (drawerState.isOpen) drawerState.close()
                    else if (showFabDismisser) onFabDismiss()
                    else if (queueSheetState.realProgress > 0) queueSheetState.collapse()
                    else if (stageSheetState.realProgress > 0) stageSheetState.animateTo(SheetValue.Collapsed, spring(1f, 400f))
                }
            }
            ScaffoldStack(
                stageProgress = stageSheetState.realProgress, fabProgress = transProgress,
                stageOffset = stageSheetState.offset.value.roundToInt(),
                queueOffset = queueSheetState.offset.value.roundToInt(),
                peekHeight = peekHeight, fabRange = fabRange,
                showBottomNav = showBottomNav, showDismisser = fabShadowAnimator > 0,
            ) {
                Box(Modifier.layoutId(LayoutId.Body), content = content)
                Shadow(Modifier.layoutId(LayoutId.FabShadow), alpha = fabShadowAnimator, color = backgroundColor)
                Box(Modifier.layoutId(LayoutId.AppBar), content = appBar)
                Shadow(Modifier.layoutId(LayoutId.StageShadow), alpha = stageSheetState.realProgress / 1.5f)
                Surface(
                    stageSwipeable.layoutId(LayoutId.Stage).fillMaxSize()
                        .clickable(stageSheetState.isCollapsed) { scope.launch { stageSheetState.expand() } },
                    shadowElevation = SheetElevation, color = stageBackgroundColor, content = stageContent
                )
                Box(Modifier.layoutId(LayoutId.BottomNav), content = bottomNav)
                Surface(
                    modifier = queueSwipeable.layoutId(LayoutId.Queue).alpha(stageSheetState.realProgress.compIn(0.9f))
                        .fillMaxSize().padding(horizontal = QueueMargin),
                    shadowElevation = SheetElevation, color = queueBackgroundColor,
                    shape = RoundedCornerShape(QueueRadius, QueueRadius, 0.dp, 0.dp),
                    content = queueContent
                )
                Dismisser(Modifier.layoutId(LayoutId.FabDismisser), showFabDismisser, onFabDismiss)
                Box(Modifier.layoutId(LayoutId.Fab), content = fab)
            }
        }
    }
}


@Composable
private fun ScaffoldStack(
    stageProgress: Float, fabProgress: Float,
    stageOffset: Int, queueOffset: Int,
    peekHeight: Int, fabRange: Dp,
    showBottomNav: Boolean, showDismisser: Boolean,
    content: @Composable @UiComposable () -> Unit,
) {
    val navAnimator by animateFloatAsState(if (showBottomNav) 1f else 0f)

    Layout(content = content) { m, c ->

        val appBar = m.first { it.layoutId == LayoutId.AppBar }.measure(c)

        val bottomNav = m.first { it.layoutId == LayoutId.BottomNav }.measure(c)

        val fab = FabSize.roundToPx().let { s -> m.first { it.layoutId == LayoutId.Fab }.measure(c.copy(s, s, s, s)) }
        val fabShadow = m.first { it.layoutId == LayoutId.FabShadow }.measure(c)
        val fabDismisser = m.first { it.layoutId == LayoutId.FabDismisser }.measure(c.copy(c.maxWidth, minHeight = c.maxHeight))

        val stageShadow = m.first { it.layoutId == LayoutId.StageShadow }.measure(c)
        val stage = m.first { it.layoutId == LayoutId.Stage }.measure(c.copy(minHeight = c.maxHeight, maxHeight = c.maxHeight))

        val queue = m.first { it.layoutId == LayoutId.Queue }.measure(c)

        val body =
            m.first { it.layoutId == LayoutId.Body }.measure(c.copy(minHeight = 0, maxHeight = c.maxHeight - appBar.height - peekHeight))


        with(c) {
            layout(maxWidth, maxHeight) {

                body.place(0, (appBar.height * (1 - stageProgress)).toInt())
                if (showDismisser) fabShadow.place(0, 0)
                appBar.place(0, -(appBar.height * stageProgress).toInt())
                if (stageProgress > 0) stageShadow.place(0, 0)
                stage.place(0, maxHeight + stageOffset)
                bottomNav.place(0, (maxHeight - navAnimator * bottomNav.height * (1 - fabProgress)).toInt())
                if (showDismisser) fabDismisser.place(0, 0)
                fab.place(
                    (maxWidth - fab.width) / 2 + ((1 - fabProgress) * (maxWidth / 2 - fab.width / 2 - FabMargin.toPx())).toInt(),
                    maxHeight + stageOffset.coerceAtMost(-(peekHeight + fabRange.roundToPx()))
                            + maxWidth + (StageSpacing + TimeLineHeight).roundToPx()
                )
                queue.place(0, maxHeight + stageOffset + queueOffset)
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
private fun Dismisser(modifier: Modifier = Modifier, active: Boolean = false, onDismiss: () -> Unit) =
    Spacer(modifier.pointerInput(active) {
        if (active) forEachGesture {
            awaitPointerEventScope {
                awaitFirstDown(requireUnconsumed = false)
                onDismiss()
            }
        }
    })