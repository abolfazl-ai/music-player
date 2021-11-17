package com.example.compose.ui.composables.layouts

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

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
