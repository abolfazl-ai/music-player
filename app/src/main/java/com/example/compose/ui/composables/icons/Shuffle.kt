package com.example.compose.ui.composables.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.Shuffle: ImageVector
    get() {
        if (_shuffle != null) {
            return _shuffle!!
        }
        _shuffle = materialIcon(name = "Filled.Artist") {
            materialPath {
                moveTo(14.83F,13.41F)
                lineTo(13.42F,14.82F)
                lineTo(16.55F,17.95F)
                lineTo(14.5F,20F)
                lineTo(20F,20F)
                lineTo(20F,14.5F)
                lineTo(17.96F,16.54F)
                lineTo(14.83F,13.41F)
                moveTo(14.5F,4F)
                lineTo(16.54F,6.04F)
                lineTo(4F,18.59F)
                lineTo(5.41F,20F)
                lineTo(17.96F,7.46F)
                lineTo(20F,9.5F)
                lineTo(20F,4F)
                moveTo(10.59F,9.17F)
                lineTo(5.41F,4F)
                lineTo(4F,5.41F)
                lineTo(9.17F,10.58F)
                lineTo(10.59F,9.17F)
                close()
            }
        }
        return _shuffle!!
    }

private var _shuffle: ImageVector? = null