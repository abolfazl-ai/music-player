package com.example.compose.ui.composables.fab

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MenuItem(
    val id: Int,
    val title: String,
    val icon: Int,
)

data class AnimParams(val offset: Dp = 0.dp, val alpha: Float = 0.8f, val scale: Float = 0.5f) {
    companion object {
        val VectorConverter: TwoWayConverter<AnimParams, AnimationVector3D> = TwoWayConverter(
            convertToVector = { AnimationVector3D(it.offset.value, it.alpha, it.scale) },
            convertFromVector = { AnimParams(Dp(it.v1), it.v2, it.v3) }
        )
    }
}

val DummyMenuItems = listOf(
    MenuItem(0, "Shuffle", R.drawable.ic_menu_shuffle),
    MenuItem(1, "Sort", R.drawable.ic_menu_sort),
    MenuItem(2, "Settings", R.drawable.ic_menu_settings),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuHandler(
    open: Boolean = false, items: List<MenuItem>, onItemClicked: (id: Int) -> Unit = {},
    shape: Shape = CircleShape, color: Color, contentColor: Color, size: Dp, spacing: Dp
) = Box(Modifier.offset(y = -spacing)) {

    val scope = rememberCoroutineScope()
    val menuAnimators = remember(items) { items.map { Animatable(AnimParams(), AnimParams.VectorConverter) } }

    LaunchedEffect(open) {
        if (!open) launch { menuAnimators.reversed().forEach { it.animateTo(AnimParams(), tween(75)) } }
        else menuAnimators.reversed().forEachIndexed { index, animatable ->
            launch {
                delay(index * 75L)
                animatable.animateTo(AnimParams((size + spacing).times(index + 1), 1f, 1f), spring(0.4f, 2_000f))
            }
        }
    }

    items.forEachIndexed { index, menuItem ->
        menuAnimators[index].value.let {

            var showHint by remember { mutableStateOf(false) }
            LaunchedEffect(showHint) { delay(2000); showHint = false }

            if (it.alpha > 0.8f) Row(
                Modifier
                    .requiredWidth(200.dp)
                    .offset(y = -it.offset, x = (-(200.dp - size) / 2)),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .alpha(animateFloatAsState(if (showHint) 1f else 0f).value),
                    shadowElevation = animateDpAsState(if (showHint) 2.dp else 0.dp, tween(delayMillis = 100)).value,
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) { Text(modifier = Modifier.padding(8.dp), text = menuItem.title, textAlign = TextAlign.Right) }

                Surface(
                    modifier = Modifier
                        .alpha(it.alpha)
                        .scale(it.scale.coerceAtMost(1f))
                        .size(size),
                    shadowElevation = 2.dp, shape = shape, color = color, contentColor = contentColor
                ) {
                    Icon(modifier = Modifier
                        .combinedClickable(onLongClick = { showHint = true }) { scope.launch { onItemClicked(menuItem.id) } }
                        .padding(4.dp), painter = painterResource(menuItem.icon), contentDescription = menuItem.title
                    )
                }
            }
        }
    }
}