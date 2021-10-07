package com.example.compose.ui.composables.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Outlined.Play: ImageVector
    get() {
        if (_play != null) {
            return _play!!
        }
        _play = materialIcon(name = "Filled.Play") {
            materialPath {
                moveTo(12F,20F)
                curveTo(7.59F,20F,4F,16.41F,4F,12F)
                curveTo(4F,7.59F,7.59F,4F,12F,4F)
                curveTo(16.41F,4F,20F,7.59F,20F,12F)
                curveTo(20F,16.41F,16.41F,20F,12F,20F)
                moveTo(12F,2F)
                curveTo(6.48F,2F,2F,6.48F,2F,12F)
                curveTo(2F,17.52F,6.48F,22F,12F,22F)
                curveTo(17.52F,22F,22F,17.52F,22F,12F)
                curveTo(22F,6.48F,17.52F,2F,12F,2F)
                moveTo(10F,16.5F)
                lineTo(16F,12F)
                lineTo(10F,7.5F)
                lineTo(10F,16.5F)
                close()
            }
        }
        return _play!!
    }

private var _play: ImageVector? = null