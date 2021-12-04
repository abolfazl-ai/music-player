package com.example.compose.ui.composables.layouts

import android.util.Log
import androidx.compose.animation.core.spring
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.ui.composables.util_composables.Shadow
import com.example.compose.utils.kotlin_extensions.compIn
import com.example.compose.utils.resources.*
import com.google.accompanist.insets.LocalWindowInsets
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
@ExperimentalAnimationGraphicsApi
@ExperimentalMaterialApi
@Composable
fun SheetScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    appBar: @Composable () -> Unit,
    bottomNav: @Composable () -> Unit,
    fab: @Composable (progress: Float) -> Unit,
    showDismiss: Boolean = false,
    onDismiss: () -> Unit = {},
    playerContent: @Composable (progress: Float) -> Unit,
    playerBackground: Color = MaterialTheme.colorScheme.primaryContainer,
    playerSheetState: SheetState = rememberSheetState(
        SheetValue.Collapsed,
        spring(stiffness = 2000f)
    ),
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
            (maxWidth + PlayerScreenSpacing.times(2) + FabSize + ProgressBarHeight).toPx() to SheetValue.Collapsed,
            LocalWindowInsets.current.statusBars.top + QueueMargin.toPx() to SheetValue.Expanded
        )
    }

    val queueSwipeable = Modifier
        .swipeable(
            state = queueSheetState,
            anchors = queueAnchors,
            orientation = Orientation.Vertical,
        )

    SheetScaffoldStack(
        width = maxWidth, height = maxHeight, pPeekHeight = realPeekHeight,
        appBar = appBar, bottomNav = bottomNav, pSheetContent = {
            Surface(
                playerSwipeable.fillMaxSize(),
                shadowElevation = SheetElevation,
                color = playerBackground,
            ) { playerContent(playerSheetState.myProgress) }
        }, pSheetOffset = { playerSheetState.offset.value },
        pSheetProgress = playerSheetState.myProgress, qSheetContent = {
            Surface(
                queueSwipeable
                    .alpha(playerSheetState.myProgress.compIn(0.8f))
                    .fillMaxSize()
                    .padding(horizontal = QueueMargin),
                shadowElevation = SheetElevation,
                color = queueBackground,
                shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
                content = queueContent
            )
        }, qSheetOffset = { queueSheetState.offset.value },
        qSheetMaxOffset = constraints.maxWidth,
        fab = fab, showDismiss = showDismiss, onDismiss = onDismiss
    ) { Surface(color = backgroundColor, content = content) }

}


@ExperimentalComposeUiApi
@Composable
private fun SheetScaffoldStack(
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
    showDismiss: Boolean,
    onDismiss: () -> Unit,
    body: @Composable () -> Unit,
) {
    val fabRange =
        remember(width) { width + FabSize + FabMargin + PlayerScreenSpacing + ProgressBarHeight }

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
            Spacer(modifier = Modifier.pointerInteropFilter { onDismiss(); true })
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

        val fabPlaceable = m.last().measure(
            c.copy(
                minWidth = FabSize.roundToPx(),
                maxWidth = FabSize.roundToPx(),
                minHeight = FabSize.roundToPx(),
                maxHeight = FabSize.roundToPx()
            )
        )

        with(c) {
            layout(maxWidth, maxHeight) {

                bodyPlaceable.place(0, (appBarPlaceable.height * (1 - pSheetProgress)).roundToInt())

                appBarPlaceable.place(0, -(appBarPlaceable.height * pSheetProgress).roundToInt())

                shadowPlaceable.place(0, 0)

                playerSheetPlaceable.place(0, maxHeight + sheetOffsetY)

                bottomNavPlaceable.place(
                    0,
                    (maxHeight - bottomNavPlaceable.height * (1 - transferProgress)).roundToInt()
                )

                if (showDismiss)
                    m[6].measure(c.copy(minWidth = c.maxWidth, minHeight = c.maxHeight)).place(0, 0)

                fabPlaceable.place(
                    (maxWidth - fabPlaceable.width) / 2 +
                            ((1 - transferProgress) * (maxWidth / 2 - fabPlaceable.width / 2 - FabMargin.toPx())).toInt(),
                    maxHeight + sheetOffsetY.coerceAtMost(-(pPeekHeight + fabRange.roundToPx())) + maxWidth +
                            (PlayerScreenSpacing + ProgressBarHeight).roundToPx()
                )

                queueSheetPlaceable.place(0, maxHeight + sheetOffsetY + qSheetOffsetY)
            }
        }
    }
}