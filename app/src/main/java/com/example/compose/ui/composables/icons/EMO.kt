package com.example.compose.ui.composables.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path

val Icons.Rounded.E: ImageVector
    get() {
        if (_E != null) {
            return _E!!
        }
        _E = materialIcon(name = "Rounded.E") {
            materialPath {
                path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f, strokeLineCap = StrokeCap.Round) {
                    moveTo(5.0f, 6.0f)
                    lineTo(19.0f, 6.0f)
                    moveTo(5.0f, 12.0f)
                    lineTo(19.0f, 12.0f)
                    moveTo(5.0f, 18.0f)
                    lineTo(19.0f, 18.0f)
                }
            }
        }
        return _E!!
    }

private var _E: ImageVector? = null


val Icons.Rounded.M: ImageVector
    get() {
        if (_M != null) {
            return _M!!
        }
        _M = materialIcon(name = "Rounded.M") {
            materialPath {
                group(pivotX = 12f, pivotY = 12f, rotate = 90f) {
                    path(stroke = SolidColor(Color.Black), strokeLineWidth = 2f, strokeLineCap = StrokeCap.Round) {
                        moveTo(7.0f, 6.0f)
                        lineTo(17.0f, 6.0f)
                        moveTo(7.0f, 12.0f)
                        lineTo(17.0f, 12.0f)
                        moveTo(7.0f, 18.0f)
                        lineTo(17.0f, 18.0f)
                    }
                }
            }
        }
        return _M!!
    }

private var _M: ImageVector? = null


val Icons.Rounded.O: ImageVector
    get() {
        if (_O != null) {
            return _O!!
        }
        _O = materialIcon(name = "Rounded.M") {
            materialPath {
                    path(
                        stroke = SolidColor(Color.Black), strokeLineWidth = 2f,
                        strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round
                    ) {
                        moveTo(6.0f, 6.0f)
                        lineTo(18.0f, 6.0f)
                        lineTo(18.0f, 18.0f)
                        lineTo(6.0f, 18.0f)
                        lineTo(6.0f, 6.0f)
                    }
            }
        }
        return _O!!
    }

private var _O: ImageVector? = null