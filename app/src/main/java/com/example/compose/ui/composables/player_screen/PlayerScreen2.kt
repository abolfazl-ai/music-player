package com.example.compose.ui.composables.player_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.Transition
import androidx.constraintlayout.compose.layoutId
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.compose.R
import com.example.compose.ui.theme.Purple500

@ExperimentalCoilApi
@Composable
fun PlayerScreen2(progress: Float) {
    MotionLayout(
        modifier = Modifier.fillMaxSize(),
        progress = progress,
        start = ConstraintSet(
            """ {
                cover: { 
                  width: "spread",
                  top: ['parent', 'top'],
                  alpha: 0.0
                },
                background: { 
                  top: ['parent', 'top',112],
                  start: ['parent', 'start'],
                  end: ['parent', 'end'],
                  bottom: ['parent', 'bottom'],
                  alpha: 0.0
                }
            } """
        ),
        end = ConstraintSet(
            """ {
                cover: { 
                  width: "spread",
                  top: ['parent', 'top'],
                  alpha: 1.0
                },
                background: { 
                  top: ['cover', 'bottom'],
                  start: ['parent', 'start'],
                  end: ['parent', 'end'],
                  bottom: ['parent', 'bottom'],
                  alpha: 1.0
                }
            } """
        ), transition = Transition(
            """{
        from: 'start',
        to: 'end',
        pathMotionArc: 'startHorizontal',
        KeyFrames: {
        KeyAttributes: [
            {
            target: ['cover'],
            frames: [0, 50, 100],
            alpha: [0.0, 0.0, 1.0],
            }
        ]
    }
            } """
        )
    ) {
        Box(
            modifier = Modifier
                .layoutId("cover", "box")
                .aspectRatio(0.5f)
                .background(Color.Black)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                painter = rememberImagePainter(data = R.drawable.cover_2),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Box( modifier = Modifier
            .layoutId("background")
            .background(Purple500)) {
            Spacer(modifier = Modifier.fillMaxSize())
        }
    }
}

class MarginShape(private val margin: Dp, private val radius: Dp) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        with(density) {
            val cornerRadius = CornerRadius(radius.toPx(), radius.toPx())
            val rRect = RoundRect(
                left = margin.toPx(),
                top = 0f,
                right = size.width - margin.toPx(),
                bottom = size.height,
                topLeftCornerRadius = cornerRadius,
                topRightCornerRadius = cornerRadius
            )
            return Outline.Rounded(rRect)
        }
    }
}


@Preview(group = "motion2")
@Composable
fun MotionExample2() {
    var animateToEnd by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (animateToEnd) 1f else 0f,
        animationSpec = tween(1000)
    )

    Column(Modifier.background(Color.White)) {
        MotionLayout(
            ConstraintSet(
                """ {
                    background: { 
                width: "spread",
                height: 60,
                start: ['parent', 'start', 16],
                bottom: ['parent', 'bottom', 16],
                end: ['parent', 'end', 16]
                },
                v1: { 
                width: 100,
                height: 60,
                start: ['parent', 'start', 16],
                bottom: ['parent', 'bottom', 16]
                },
                title: { 
                width: "spread",
                start: ['v1', 'end', 8],
                top: ['v1', 'top', 8],
                end: ['parent', 'end', 8],
                custom: {
                  textSize: 16
                }
                },
                description: { 
                start: ['v1', 'end', 8],
                top: ['title', 'bottom', 0],
                custom: {
                  textSize: 14
                }
                },
                list: { 
                width: "spread",
                height: 0,
                start: ['parent', 'start', 8],
                end: ['parent', 'end', 8],
                top: ['parent', 'bottom', 0]
                },
                play: { 
                end: ['close', 'start', 8],
                top: ['v1', 'top', 0],
                bottom: ['v1', 'bottom', 0]
                },
                close: { 
                end: ['parent', 'end', 24],
                top: ['v1', 'top', 0],
                bottom: ['v1', 'bottom', 0]
                }
            } """
            ),
            ConstraintSet(
                """ {
                background: { 
                width: "spread",
                height: 250,
                start: ['parent', 'start', 0],
                end: ['parent', 'end', 0],
                top: ['parent', 'top', 0]
                },
                v1: { 
                width: "spread",
                height: 250,
                start: ['parent', 'start', 0],
                end: ['parent', 'end', 0],
                top: ['parent', 'top', 0]
                },
                title: { 
                width: "spread",
                height: 28,
                start: ['parent', 'start', 16],
                top: ['v1', 'bottom', 16],
                end: ['parent', 'end', 16],
                custom: {
                  textSize: 20
                }
                },
                description: { 
                width: "spread",
                start: ['parent', 'start', 16],
                top: ['title', 'bottom', 8],
                end: ['parent', 'end', 16],
                custom: {
                  textSize: 16
                }
                },
                list: { 
                width: "spread",
                height: 400,
                start: ['parent', 'start', 16],
                end: ['parent', 'end', 16],
                top: ['description', 'bottom', 16]
                },
                play: { 
                start: ['parent', 'end', 8],
                top: ['v1', 'top', 0],
                bottom: ['v1', 'bottom', 0]
                },
                close: { 
                start: ['parent', 'end', 8],
                top: ['v1', 'top', 0],
                bottom: ['v1', 'bottom', 0]
                }
            } """
            ),
            progress = progress,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .layoutId("background", "box")
                    .background(Color.Cyan)
                    .clickable(onClick = { animateToEnd = !animateToEnd })
            )
            Button(
                onClick = { animateToEnd = !animateToEnd },
                modifier = Modifier
                    .layoutId("v1", "box")
                    .background(Color.Blue)
            ) {}

            Text(
                text = "MotionLayout in Compose",
                modifier = Modifier.layoutId("title"),
                color = Color.Black,
                fontSize = motionProperties("title").value.fontSize("textSize")
            )
            Text(
                text = "Demo screen 17",
                modifier = Modifier.layoutId("description"),
                color = Color.Black,
                fontSize = motionProperties("description").value.fontSize("textSize")
            )
            Box(
                modifier = Modifier
                    .layoutId("list", "box")
                    .background(Color.Gray)
            )
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = "Play",
                tint = Color.Black,
                modifier = Modifier.layoutId("play")
            )

            Icon(
                Icons.Filled.Close,
                contentDescription = "Close",
                tint = Color.Black,
                modifier = Modifier.layoutId("close")
            )

        }
    }
}