package com.example.compose.ui.composables

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.utils.util_classes.FabState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class SheetValue { Collapsed, Expanded }

@ExperimentalMaterialApi
@Stable
class SheetState(
    initialValue: SheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (SheetValue) -> Boolean = { true },
) : SwipeableState<SheetValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {

    var maxOffset: Float = 0f

    val fraction = offset.value / maxOffset

    val isExpanded: Boolean get() = currentValue == SheetValue.Expanded

    val isCollapsed: Boolean get() = currentValue == SheetValue.Collapsed

    suspend fun expand() = animateTo(SheetValue.Expanded)

    suspend fun collapse() = animateTo(SheetValue.Collapsed)

    companion object {
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (SheetValue) -> Boolean
        ): Saver<SheetState, *> = Saver(
            save = { it.currentValue },
            restore = {
                SheetState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmStateChange = confirmStateChange
                )
            }
        )
    }
}

@Composable
@ExperimentalMaterialApi
fun rememberSheetState(
    initialValue: SheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (SheetValue) -> Boolean = { true }
): SheetState {
    return rememberSaveable(
        animationSpec,
        saver = SheetState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    ) {
        SheetState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun SheetLayout(
    modifier: Modifier = Modifier,
    playScreenContent: @Composable BoxScope.() -> Unit,
    playerSheetState: SheetState = rememberSheetState(SheetValue.Collapsed),
    queueContent: @Composable BoxScope.() -> Unit,
    queueSheetState: SheetState = rememberSheetState(SheetValue.Collapsed),
    topBar: @Composable () -> Unit,
    bottomAppBar: @Composable () -> Unit,
    fab: (@Composable () -> Unit)? = null,
    sheetElevation: Dp = BottomSheetScaffoldDefaults.SheetElevation,
    sheetBackgroundColor: Color = Color.Black,
    sheetPeekHeight: Dp = 100.dp,
    backgroundColor: Color = MaterialTheme.colors.background,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    BoxWithConstraints(modifier) {
        val fullHeight = constraints.maxHeight.toFloat()
        val peekHeightPx = with(LocalDensity.current) { sheetPeekHeight.toPx() }

        val swipeable = Modifier
            .swipeable(
                state = playerSheetState,
                anchors = mapOf(
                    fullHeight - peekHeightPx to SheetValue.Collapsed,
                    0f to SheetValue.Expanded
                ),
                orientation = Orientation.Vertical,
                resistance = null
            )
            .semantics {
                if (playerSheetState.isCollapsed) {
                    expand {
                        scope.launch { playerSheetState.expand() }
                        true
                    }
                } else {
                    collapse {
                        scope.launch { playerSheetState.collapse() }
                        true
                    }
                }
            }

        BottomSheetScaffoldStack(
            body = {
                Surface(color = backgroundColor) {
                    Column(Modifier.fillMaxSize()) {
                        content(PaddingValues(bottom = sheetPeekHeight))
                    }
                }
            },
            topBar, bottomAppBar,
            bottomSheet = {
                Surface(
                    swipeable.fillMaxSize(),
                    elevation = sheetElevation,
                    color = sheetBackgroundColor,
                    content = { Box(content = playScreenContent) }
                )
            },
            floatingActionButton = { DraggableFab(state = FabState.Menu) },
            sheetOffset = playerSheetState.offset,
            (fullHeight - peekHeightPx).toInt()
        )
    }
}

@Composable
private fun BottomSheetScaffoldStack(
    body: @Composable () -> Unit,
    topBar: @Composable () -> Unit,
    bottomAppBar: @Composable () -> Unit,
    bottomSheet: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    sheetOffset: State<Float>,
    maxOffset: Int,
    fabMargin: Dp = 16.dp,
    fabSize: Dp = 56.dp,
    playButtonMargin: Dp = 12.dp,
) {
    Layout(
        content = {
            body()
            topBar()
            bottomSheet()
            bottomAppBar()
            floatingActionButton()
        }
    ) { measurables, constraints ->

        layout(constraints.maxWidth, constraints.maxHeight) {

            val (bodyPlaceable, topBarPlaceable, sheetPlaceable, bottomNavPlaceable, fabPlaceable) =
                measurables.map { it.measure(constraints) }

            with(constraints) {

            bodyPlaceable.place(0, topBarPlaceable.height)
            topBarPlaceable.place(0, 0)

            val sheetOffsetY = sheetOffset.value.roundToInt()
            sheetPlaceable.place(0, sheetOffsetY)

                (maxWidth + (fabMargin + fabSize + playButtonMargin).roundToPx()).let {

                val transferProgress = ((sheetOffsetY.coerceAtLeast(maxOffset - it) -
                        (maxOffset - it)) * 1f / it)

            bottomNavPlaceable.place(0, (maxHeight - transferProgress * bottomNavPlaceable.height).toInt())

                    fabPlaceable.place((maxWidth - fabSize.roundToPx()) / 2 +
                                (transferProgress * (maxWidth / 2 - (fabSize / 2 + fabMargin).toPx())).toInt(),
                        sheetOffsetY.coerceAtMost(maxOffset - it) + maxWidth + 12.dp.roundToPx()
                    )
                }
            }
        }
    }
}