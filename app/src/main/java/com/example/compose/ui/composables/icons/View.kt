package com.example.compose.ui.composables.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.View: ImageVector
    get() {
        if (_view != null) {
            return _view!!
        }
        _view = materialIcon(name = "Filled.Artist") {
            materialPath {
                moveTo(2F, 3F)
                lineTo(2F, 6F)
                lineTo(21F, 6F)
                lineTo(21F, 3F)
                moveTo(20F, 8F)
                lineTo(3F, 8F)
                curveTo(2.45F, 8F, 2F, 8.45F, 2F, 9F)
                lineTo(2F, 15F)
                curveTo(2F, 15.55F, 2.45F, 16F, 3F, 16F)
                lineTo(20F, 16F)
                curveTo(20.55F, 16F, 21F, 15.55F, 21F, 15F)
                lineTo(21F, 9F)
                curveTo(21F, 8.45F, 20.55F, 8F, 20F, 8F)
                moveTo(2F, 21F)
                lineTo(21F, 21F)
                lineTo(21F, 18F)
                lineTo(2F, 18F)
                lineTo(2F, 21F)
                close()
            }
        }
        return _view!!
    }

private var _view: ImageVector? = null