package com.example.compose.ui.composables.tablayout

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ui.OptionItem
import com.example.compose.ui.Screen
import com.example.compose.ui.getScreenComposable
import com.example.compose.viewmodel.MainViewModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.round
import kotlin.math.sign

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class,
    ExperimentalAnimationGraphicsApi::class
)
@Composable
fun HomeTabLayout(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) = Box(modifier, contentAlignment = Alignment.BottomCenter) {

    var fabExpanded by remember { mutableStateOf(false) }
    val state by viewModel.uiState.collectAsState()
    var width = remember { 0 }
    val pagerState = rememberPagerState()

    HorizontalPager(state = pagerState,count = state.homeScreens.size) { page ->
        getScreenComposable(state.homeScreens[page]).invoke(
            Modifier
                .onGloballyPositioned { c -> width = c.size.width }
                .graphicsLayer {
                    with(calculateCurrentOffsetForPage(page)) {
                        if (this in -1f..1f) {
                            translationX = transX * width
                            alpha = 1 - 2f * absoluteValue.coerceIn(0f, 0.5f)
                        }
                    }
                })
    }

    AnimatedVisibility(visible = fabExpanded, enter = fadeIn(), exit = fadeOut()) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter { fabExpanded = false; true }
                .alpha(0.7f)
                .background(MaterialTheme.colors.background)
        )
    }

    TabLayout(
        screens = state.homeScreens,
        pagerState = pagerState,
        fabExpanded = fabExpanded
    ) { fabExpanded = !fabExpanded }
}

private val Float.transX
    get() = (coerceIn(-0.4f, 0.4f) - sign * (absoluteValue.coerceIn(0.6f, 1f) - 0.6f))


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun TabLayout(
    modifier: Modifier = Modifier,
    screens: List<Screen> = emptyList(),
    pagerState: PagerState = rememberPagerState(),
    horPadding: Dp = 24.dp,
    fabExpanded: Boolean = false,
    color: Color = MaterialTheme.colors.surface,
    onFabExpand: () -> Unit = {}
) = BoxWithConstraints(modifier, contentAlignment = Alignment.BottomCenter) {

    val offset: Float = remember(pagerState.currentPageOffset) {
        pagerState.currentPage + pagerState.currentPageOffset
    }
    val tabCount = remember { pagerState.pageCount }
    val coroutineScope = rememberCoroutineScope()

    if (offset in 0f..tabCount.toFloat() && screens.size == tabCount) {

        val tabHeight = remember { 56.dp }
        val indSize = remember { 54.dp }
        val iconSize = remember { 44.dp }

        val tabWidth = remember { (maxWidth - horPadding.times(2) - iconSize).div(tabCount - 1) }
        val x = remember(offset) { horPadding + iconSize.div(2) + tabWidth.times(offset) }
        val scale = remember(x) {
            pagerState.currentPageOffset.let {
                if (abs(round(it) - it) < 0.4f) 1 - abs(round(it) - it) else 0.6f
            }
        }
        val alpha = remember(x) { (scale - 0.6f) * 2.5f }

        val path = with(LocalDensity.current) {
            remember(x) {
                Path().apply {

                    val totalH = 72.dp
                    val tabY = (totalH - tabHeight).toPx()
                    val r = 56.dp
                    val h = 22.dp

                    fillType = PathFillType.EvenOdd

                    moveTo(0f, totalH.toPx())
                    lineTo(0f, tabY)
                    lineTo((x - r).toPx(), tabY)
                    cubicTo((x - r / 2).toPx(), tabY, (x - h).toPx(), 0f, x.toPx(), 0f)
                    cubicTo((x + h).toPx(), 0f, (x + r / 2).toPx(), tabY, (x + r).toPx(), tabY)
                    lineTo(maxWidth.toPx(), tabY)
                    lineTo(maxWidth.toPx(), totalH.toPx())
                    close()

                    val indCenter = Offset(x.toPx(), (totalH - indSize / 2 - 10.dp).toPx())
                    addOval(Rect(indCenter, (indSize / 1.85f).toPx()))
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(72.dp)
                .drawShadow(Color.Black, path = path)
        ) { drawPath(path, Color.White.copy(0.1f).compositeOver(color)) }

        Row(
            modifier = Modifier
                .height(tabHeight)
                .fillMaxWidth()
                .padding(horPadding, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            screens.forEachIndexed { i, screen ->
                Icon(
                    modifier = Modifier
                        .size(iconSize)
                        .alpha(if (abs(offset - i) >= 1) 1f else 3f * (abs(offset - i) - .67f))
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable(interactionSource, null) {
                            coroutineScope.launch {
                                if (fabExpanded) onFabExpand() else pagerState.animateScrollToPage(i)
                            }
                        }
                        .padding(10.dp),
                    imageVector = screen.icon,
                    tint = MaterialTheme.colors.onSurface,
                    contentDescription = null
                )
            }
        }

        Indicator(
            Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 10.dp)
                .offset(x - indSize / 2),
            size = indSize,
            scale = scale,
            icon = screens[round(offset).toInt()].icon,
            options = screens[round(offset).toInt()].options,
            color = middleColorCalculator(screens.map { it.color }, offset = offset),
            alpha, fabExpanded, onFabExpand
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun Indicator(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    scale: Float = 1f,
    icon: ImageVector,
    options: List<OptionItem>,
    color: Color,
    alpha: Float,
    fabExpanded: Boolean = false,
    onFabExpand: () -> Unit = {}
) {

    Surface(
        modifier = modifier
            .width(size)
            .scale(scale),
        contentColor = Color.White,
        shape = RoundedCornerShape(size / 2),
        color = Color.Black.copy(0.15F).compositeOver(color),
        elevation = 4.dp
    ) {
        Column {
            AnimatedContent(targetState = fabExpanded, transitionSpec = {
                fadeIn(animationSpec = tween(220, delayMillis = 120)) +
                        scaleIn(tween(220, delayMillis = 120), 0.8f) with
                        fadeOut(animationSpec = tween(70, 80)) using
                        SizeTransform { _, _ ->
                            if (targetState) spring(0.7f, 500f) else tween(150, 200)
                        }
            }) { targetExpanded ->
                if (targetExpanded) {
                    Column(Modifier.padding(4.dp)) {
                        options.forEach {
                            Icon(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .clickable { onFabExpand() }
                                    .padding(12.dp),
                                imageVector = it.icon,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
            AnimatedContent(modifier = Modifier
                .alpha(alpha)
                .size(size)
                .clip(CircleShape)
                .background(color)
                .clickable(onClick = onFabExpand)
                .padding(14.dp), targetState = fabExpanded, transitionSpec = {
                (slideInVertically(spring(stiffness = 2500F)) { height -> if (targetState) height else -height } +
                        fadeIn(spring(stiffness = 400F)) with
                        slideOutVertically { height -> if (targetState) -height else height } +
                        fadeOut(spring(stiffness = 10_000F)))
                    .using(SizeTransform())
            }) { target ->
                Icon(
                    imageVector = if (target) Icons.Default.Close else icon,
                    contentDescription = null
                )
            }
        }
    }
}


fun Modifier.drawShadow(
    color: Color,
    alpha: Float = 0.3f,
    shadowRadius: Dp = 4.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    path: Path
) = this.drawBehind {
    val transparentColor =
        android.graphics.Color.toArgb(color.copy(alpha = 0.0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawPath(path, paint)
    }
}

fun middleColorCalculator(colors: List<Color>, offset: Float): Color {

    if (offset >= colors.lastIndex || offset < 0f) return colors[offset.toInt()]

    val absOffset = offset % 1
    val from = colors[offset.toInt()]
    val to = colors[offset.toInt() + 1]

    val red = (255 * ((1 - absOffset) * from.red + absOffset * to.red)).toInt()
    val gre = (255 * ((1 - absOffset) * from.green + absOffset * to.green)).toInt()
    val blu = (255 * ((1 - absOffset) * from.blue + absOffset * to.blue)).toInt()

    return Color(red, gre, blu, alpha = 255)
}