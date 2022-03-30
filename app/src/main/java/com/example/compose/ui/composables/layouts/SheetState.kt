package com.example.compose.ui.composables.layouts

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.compose.ui.composables.layouts.SheetValue.*

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

    val isExpanded: Boolean get() = realProgress == 1f

    val isCollapsed: Boolean get() = realProgress == 0f

    suspend fun expand() = animateTo(Expanded)

    suspend fun collapse() = animateTo(Collapsed)

    val realProgress: Float
        get() = progress.run {
            when {
                from == Collapsed && to == Collapsed -> 0f
                from == Expanded && to == Expanded -> 1f
                from == Collapsed && to == Expanded -> fraction
                else -> 1f - fraction
            }
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
