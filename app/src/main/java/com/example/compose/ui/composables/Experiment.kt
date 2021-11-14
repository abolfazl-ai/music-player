package com.example.compose.ui.composables

import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.utils.resources.*
import com.google.accompanist.insets.LocalWindowInsets
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

    val myProgress: Float
        get() {
            return if (progress.from == progress.to) {
                if (progress.from == SheetValue.Expanded) 1f else 0f
            } else if (progress.fraction != 1f && progress.fraction != 0f) {
                if (progress.from == SheetValue.Collapsed) progress.fraction else (1 - progress.fraction)
            } else if (isCollapsed) 0f else 1f
        }

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
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    appBar: @Composable () -> Unit,
    bottomNav: @Composable () -> Unit,
    fab: @Composable (progress: Float) -> Unit,
    playerContent: @Composable (progress: Float) -> Unit,
    playerBackground: Color = MaterialTheme.colorScheme.primaryContainer,
    playerSheetState: SheetState = rememberSheetState(SheetValue.Collapsed),
    queueContent: @Composable () -> Unit,
    queueSheetState: SheetState = rememberSheetState(SheetValue.Collapsed),
    queueBackground: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit
) = BoxWithConstraints(modifier.fillMaxSize()) {

    val realPeekHeight = with(LocalDensity.current) {
        (PlayerSheetPeekHeight + BottomNavHeight).roundToPx()
    } + LocalWindowInsets.current.navigationBars.bottom

    val playerSwipeable = Modifier
        .swipeable(
            state = playerSheetState,
            anchors = mapOf(
                -realPeekHeight.toFloat() to SheetValue.Collapsed,
                -constraints.maxHeight.toFloat() to SheetValue.Expanded,
            ),
            orientation = Orientation.Vertical,
            resistance = null
        )

    val queueAnchors = with(LocalDensity.current) {
        mapOf(
            (maxWidth + PlayerScreenSpacing.times(2) + FabSize).toPx() to SheetValue.Collapsed,
            LocalWindowInsets.current.statusBars.top + QueueMargin.toPx() to SheetValue.Expanded
        )
    }

    val queueSwipeable = Modifier
        .swipeable(
            state = queueSheetState,
            anchors = queueAnchors,
            orientation = Orientation.Vertical,
        )

    BottomSheetScaffoldStack(
        width = maxWidth, height = maxHeight, pPeekHeight = realPeekHeight,
        appBar = appBar, bottomNav = bottomNav,
        pSheetContent = {
            Surface(
                playerSwipeable.fillMaxSize(),
                shadowElevation = SheetElevation,
                color = playerBackground,
            ) { playerContent(playerSheetState.myProgress) }
        },
        pSheetOffset = { playerSheetState.offset.value },
        pSheetProgress = playerSheetState.myProgress,
        qSheetContent = {
            Surface(
                queueSwipeable
                    .fillMaxSize()
                    .padding(horizontal = QueueMargin),
                shadowElevation = SheetElevation,
                color = queueBackground,
                shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
                content = queueContent
            )
        },
        qSheetOffset = { queueSheetState.offset.value },
        qSheetMaxOffset = constraints.maxWidth,
        fab = fab,
    ) { Surface(color = backgroundColor, content = content) }

}

@Composable
private fun BottomSheetScaffoldStack(
    width: Dp, height: Dp, pPeekHeight: Int,
    appBar: @Composable () -> Unit,
    bottomNav: @Composable () -> Unit,
    pSheetContent: @Composable () -> Unit,
    pSheetOffset: () -> Float,
    pSheetProgress: Float,
    qSheetContent: @Composable () -> Unit,
    qSheetOffset: () -> Float,
    qSheetMaxOffset: Int,
    fab: @Composable (progress: Float) -> Unit,
    fabMargin: Dp = 16.dp,
    playButtonMargin: Dp = 12.dp,
    body: @Composable () -> Unit,
) {
    val fabRange = remember(width) { width + FabSize + FabMargin + PlayerScreenSpacing }

    val transferProgress = with(LocalDensity.current) {
        -(pSheetOffset().roundToInt().coerceIn(-pPeekHeight - fabRange.roundToPx(), -pPeekHeight) +
                pPeekHeight) / fabRange.toPx()
    }

    Layout(
        content = {
            body()
            appBar()
            Shadow(alpha = pSheetProgress / 1.5f)
            pSheetContent()
            bottomNav()
            qSheetContent()
            fab(transferProgress)
        }
    ) { m, c ->

        val sheetOffsetY = pSheetOffset().roundToInt()
        val qSheetOffsetY = qSheetOffset().roundToInt()

        val appBarPlaceable = m[1].measure(c)

        val bodyPlaceable = m[0].measure(
            c.copy(minHeight = 0, maxHeight = c.maxHeight - appBarPlaceable.height - pPeekHeight)
        )

        val shadowPlaceable = m[2].measure(
            c.copy(minHeight = c.maxHeight - pPeekHeight, maxHeight = c.maxHeight - pPeekHeight)
        )

        val playerSheetPlaceable = m[3].measure(
            c.copy(minHeight = c.maxHeight, maxHeight = c.maxHeight)
        )

        val bottomNavPlaceable = m[4].measure(c)

        val queueSheetPlaceable = m[5].measure(c)

        val fabPlaceable = m[6].measure(
            c.copy(
                minWidth = FabSize.roundToPx(),
                maxWidth = FabSize.roundToPx(),
                minHeight = FabSize.roundToPx(),
                maxHeight = FabSize.roundToPx()
            )
        )


        Log.e(TAG, "SheetLayout: $transferProgress")

        with(c) {
            layout(maxWidth, maxHeight) {

                bodyPlaceable.place(0, (appBarPlaceable.height * (1 - pSheetProgress)).roundToInt())

                shadowPlaceable.place(0, 0)

                playerSheetPlaceable.place(0, maxHeight + sheetOffsetY)

                queueSheetPlaceable.place(0, maxHeight + sheetOffsetY + qSheetOffsetY)

                bottomNavPlaceable.place(
                    0,
                    (maxHeight - bottomNavPlaceable.height * (1 - transferProgress)).roundToInt()
                )

                appBarPlaceable.place(0, -(appBarPlaceable.height * pSheetProgress).roundToInt())

                fabPlaceable.place(
                    (maxWidth - fabPlaceable.width) / 2 +
                            ((1 - transferProgress) * (maxWidth / 2 - fabPlaceable.width / 2 - fabMargin.toPx())).toInt(),
                    maxHeight + sheetOffsetY.coerceAtMost(-(pPeekHeight + fabRange.roundToPx())) + maxWidth + PlayerScreenSpacing.roundToPx()
                )
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