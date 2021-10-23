package com.example.compose.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.compose.utils.kotlin_extensions.progress
import com.example.compose.utils.util_classes.FabState
import com.example.compose.utils.util_classes.FabState.*

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.background,
    sheetPeekHeight: Dp = 56.dp,
    queuePeekHeight: Dp = 56.dp,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    playerScreen: @Composable () -> Unit = {},
    miniPlayer: @Composable (alpha: Float) -> Unit = {},
    queueContent: @Composable () -> Unit = {},
    bottomNav: @Composable () -> Unit = {},
    appBar: @Composable () -> Unit = {},
    fab: @Composable (FabState) -> Unit = { },
    fabMargin: Dp = 16.dp,
    fabSize: Dp = 56.dp,
    playButtonMargin: Dp = 12.dp,
    showDismissView: Boolean = false,
    onDismiss: () -> Unit = {},
    mainContent: @Composable (PaddingValues) -> Unit = {},
) = BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

    BottomSheetScaffold(
        modifier = modifier, scaffoldState = scaffoldState,
        backgroundColor = backgroundColor, sheetContent = {
            Box {
                playerScreen()
                scaffoldState.bottomSheetState.progress().run {
                    if (this < 1)
                        Surface(
                            Modifier
                                .fillMaxSize()
                                .alpha(1 - 5 * (coerceIn(0.2f, 0.4f) - 0.2f))
                        ) {
                            miniPlayer(1 - 10 * (coerceIn(0f, 0.1f)))
                        }
                }
            }
        }, sheetPeekHeight = sheetPeekHeight
    ) {
        Box(Modifier.fillMaxSize()) {
            mainContent(
                PaddingValues(
                    top = 82.dp + it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                )
            )
            appBar()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .offset { IntOffset(0, (1860 - scaffoldState.bottomSheetState.offset.value).toInt()) },
        elevation = 6.dp,
        content = bottomNav
    )

    if (showDismissView) Spacer(modifier = Modifier
        .fillMaxSize()
        .pointerInteropFilter { onDismiss();true })

    val maxOffset = remember { maxHeight - sheetPeekHeight }
    val fabMoveRange = remember { maxWidth + fabMargin + playButtonMargin + fabSize }
    val startOffset = remember { maxOffset - fabMoveRange }

    Box(
        Modifier
            .padding(bottom = sheetPeekHeight + fabMargin)
            .offset {
                with(scaffoldState.bottomSheetState.offset.value) {
                    IntOffset(
                        (((coerceIn(
                            startOffset.toPx(),
                            maxOffset.toPx()
                        ) - startOffset.toPx()) / fabMoveRange.toPx()) *
                                ((maxWidth - 56.dp) / 2 - 16.dp).toPx()).toInt(),
                        (coerceIn(0f, startOffset.toPx()) - startOffset.toPx()).toInt()
                    )
                }
            },
    ) {
        fab(
            when (scaffoldState.bottomSheetState.progress()) {
                0f -> Menu
                1f -> Play
                else -> with(LocalDensity.current) {
                    MenuToPlay(
                        (1 - (scaffoldState.bottomSheetState.offset.value.coerceIn(
                            startOffset.toPx(),
                            maxOffset.toPx()
                        ) - startOffset.toPx()) / fabMoveRange.toPx())
                    )
                }

            }
        )
    }
}