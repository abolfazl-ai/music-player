package com.example.compose.ui.composables

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.utils.resources.*
import com.google.accompanist.insets.LocalWindowInsets
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

    val isExpanded: Boolean get() = currentValue == SheetValue.Expanded

    val isCollapsed: Boolean get() = currentValue == SheetValue.Collapsed

    suspend fun expand() = animateTo(SheetValue.Expanded)

    suspend fun collapse() = animateTo(SheetValue.Collapsed)

    companion object {
        fun saver(
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
        saver = SheetState.saver(
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
    backgroundColor: Color = MaterialTheme.colors.background,
    appBar: @Composable () -> Unit,
    bottomNav: @Composable () -> Unit,
    fab: @Composable () -> Unit,
    playerContent: @Composable (progress: Float) -> Unit,
    playerBackground: Color = MaterialTheme.colors.surface,
    playerSheetState: SheetState = rememberSheetState(SheetValue.Collapsed),
    queueContent: @Composable () -> Unit,
    queueSheetState: SheetState = rememberSheetState(SheetValue.Collapsed),
    queueBackground: Color = MaterialTheme.colors.surface,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    BoxWithConstraints(modifier) {

        val maxOffset = with(LocalDensity.current) {
            constraints.maxHeight - PlayerSheetPeekHeight.toPx() -
                    LocalWindowInsets.current.navigationBars.bottom - BottomNavHeight.toPx()
        }

        val playerSwipeable = Modifier
            .swipeable(
                state = playerSheetState,
                anchors = mapOf(
                    maxOffset to SheetValue.Collapsed,
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

        val queueAnchors = with(LocalDensity.current) {
            mapOf(
                constraints.maxWidth + (PlayerScreenSpacing.times(2) + FabSize).toPx() to SheetValue.Collapsed,
                LocalWindowInsets.current.statusBars.top + QueueMargin.toPx() to SheetValue.Expanded
            )
        }

        val queueSwipeable = Modifier
            .swipeable(
                state = queueSheetState,
                anchors = queueAnchors,
                orientation = Orientation.Vertical,
//                resistance = null
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
            appBar, bottomNav,
            pSheetContent = {
                Surface(
                    playerSwipeable.fillMaxSize(),
                    elevation = SheetElevation,
                    color = playerBackground,
                ) { playerContent(it) }
            },
            pSheetOffset = playerSheetState.offset,
            pSheetMaxOffset = maxOffset.toInt(),
            qSheetContent = {
                Surface(
                    queueSwipeable
                        .fillMaxSize()
                        .padding(horizontal = QueueMargin),
                    elevation = SheetElevation,
                    color = queueBackground,
                    shape = RoundedCornerShape(8.dp,8.dp,0.dp,0.dp),
                    content = queueContent
                )
            },
            qSheetOffset = queueSheetState.offset,
            qSheetMaxOffset = constraints.maxWidth,
            fab = fab,
        ) { Surface(color = backgroundColor, content = content) }
    }
}

@Composable
private fun BottomSheetScaffoldStack(
    topBar: @Composable () -> Unit,
    bottomNav: @Composable () -> Unit,
    pSheetContent: @Composable (progress: Float) -> Unit,
    pSheetOffset: State<Float>,
    pSheetMaxOffset: Int,
    qSheetContent: @Composable () -> Unit,
    qSheetOffset: State<Float>,
    qSheetMaxOffset: Int,
    fab: @Composable () -> Unit,
    fabMargin: Dp = 16.dp,
    playButtonMargin: Dp = 12.dp,
    body: @Composable () -> Unit,
) {
    val playerSheetProgress = pSheetOffset.value / pSheetMaxOffset

    Layout(
        content = {
            body()
            topBar()
            Shadow(alpha = (1 - playerSheetProgress) / 1.5f)
            pSheetContent(playerSheetProgress)
            bottomNav()
            qSheetContent()
            fab()
        }
    ) { measurables, constraints ->

        layout(constraints.maxWidth, constraints.maxHeight) {

            val (topBarPlaceable, shadowPlaceable, sheetPlaceable, bottomNavPlaceable, qSheetPlaceable, fabPlaceable) =
                measurables.minus(measurables[0]).map { it.measure(constraints) }

            val bodyPlaceable =
                measurables[0].measure(constraints.copy(maxHeight = pSheetMaxOffset - topBarPlaceable.height))

            with(constraints) {

                bodyPlaceable.place(0, topBarPlaceable.height)
                topBarPlaceable.place(0, 0)

                shadowPlaceable.place(0, 0)

                val sheetOffsetY = pSheetOffset.value.roundToInt()
                sheetPlaceable.place(0, sheetOffsetY)

                val qSheetOffsetY = qSheetOffset.value.roundToInt()
                qSheetPlaceable.place(0, (1.2f * sheetOffsetY + qSheetOffsetY).roundToInt())

                (maxWidth + fabPlaceable.width + (fabMargin + playButtonMargin).roundToPx()).let {

                    val transferProgress = ((sheetOffsetY.coerceAtLeast(pSheetMaxOffset - it) -
                            (pSheetMaxOffset - it)) * 1f / it)

                    bottomNavPlaceable.place(
                        0,
                        (maxHeight - bottomNavPlaceable.height + (pSheetMaxOffset - sheetOffsetY))
                    )

                    fabPlaceable.place(
                        (maxWidth - fabPlaceable.width) / 2 +
                                (transferProgress * (maxWidth / 2 - fabPlaceable.width / 2 - fabMargin.toPx())).toInt(),
                        sheetOffsetY.coerceAtMost(pSheetMaxOffset - it) + maxWidth + 12.dp.roundToPx()
                    )
                }
            }
        }
    }
}

operator fun <T> List<T>.component6(): T = get(5)
operator fun <T> List<T>.component7(): T = get(6)

@Composable
fun Shadow(modifier: Modifier = Modifier, alpha: Float) = Spacer(
    modifier
        .fillMaxSize()
        .alpha(alpha)
        .background(Color.Black)
)