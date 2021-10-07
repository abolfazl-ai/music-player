package com.example.compose.ui.composables.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.Play: ImageVector
    get() {
        if (_play != null) {
            return _play!!
        }
        _play = materialIcon(name = "Filled.Play") {
            materialPath {
                moveTo(7.7f, 7.7f);
                lineTo(7.7f, 16.2f);
                curveTo(7.7f, 17.4f, 9f, 18.1f, 10f, 17.5f);
                lineTo(16.7f, 13.2f);
                curveTo(17.6f, 12.6f, 17.6f, 11.3f, 16.7f, 10.7f);
                lineTo(10f, 6.5f);
                curveTo(9f, 5.9f, 7.7f, 6.6f, 7.7f, 7.7f);
                close()
            }
        }
        return _play!!
    }

private var _play: ImageVector? = null