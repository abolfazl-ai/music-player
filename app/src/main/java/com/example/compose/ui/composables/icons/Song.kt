package com.example.compose.ui.composables.icons

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

val Icons.Filled.Song: ImageVector
    get() {
        if (_song != null) {
            return _song!!
        }
        _song = materialIcon(name = "Filled.Artist") {
            materialPath {
                moveTo(17.9f, 3.1f)
                lineTo(17.9f, 16.2f)
                curveTo(17.9f, 17.8f, 16.6f, 19f, 15f, 19f)
                curveTo(13.4f, 19f, 12.1f, 17.7f, 12.1f, 16.2f)
                curveTo(12f, 14.6f, 13.4f, 13.4f, 15f, 13.4f)
                lineTo(16.3f, 13.4f)
                lineTo(16.3f, 7.5f)
                curveTo(16.3f, 7.3f, 16f, 7.1f, 15.8f, 7.2f)
                lineTo(8.2f, 9.4f)
                curveTo(8f, 9.5f, 7.8f, 9.7f, 7.8f, 10f)
                lineTo(7.8f, 18.7f)
                curveTo(7.8f, 20.3f, 6.5f, 21.5f, 4.9f, 21.5f)
                curveTo(3.3f, 21.5f, 2f, 20.2f, 2f, 18.7f)
                curveTo(2f, 17.2f, 3.3f, 15.9f, 4.9f, 15.9f)
                curveTo(5.4f, 15.9f, 5.8f, 16f, 6.2f, 16.2f)
                lineTo(6.2f, 6.3f)
                curveTo(6.2f, 5.8f, 6.5f, 5.4f, 7f, 5.2f)
                lineTo(17.1f, 2.5f)
                curveTo(17.5f, 2.4f, 17.9f, 2.7f, 17.9f, 3.1f)
                close()

                moveTo(4.2f, 9.8f)
                lineTo(1.8f, 9.8f)
                curveTo(1.4f, 9.8f, 1f, 9.4f, 1f, 9f)
                curveTo(1f, 8.6f, 1.4f, 8.2f, 1.8f, 8.2f)
                lineTo(4.2f, 8.2f)
                curveTo(4.6f, 8.2f, 5f, 8.6f, 5f, 9f)
                curveTo(5f, 9.4f, 4.6f, 9.8f, 4.2f, 9.8f)
                close()

                moveTo(4.2f, 13.8f)
                lineTo(1.8f, 13.8f)
                curveTo(1.4f, 13.8f, 1f, 13.4f, 1f, 13f)
                curveTo(1f, 12.6f, 1.4f, 12.2f, 1.8f, 12.2f)
                lineTo(4.2f, 12.2f)
                curveTo(4.6f, 12.2f, 5f, 12.6f, 5f, 13f)
                curveTo(5f, 13.4f, 4.6f, 13.8f, 4.2f, 13.8f)
                close()

                moveTo(22.2f, 9.8f)
                lineTo(19.8f, 9.8f)
                curveTo(19.4f, 9.8f, 19f, 9.4f, 19f, 9f)
                curveTo(19f, 8.6f, 19.4f, 8.2f, 19.8f, 8.2f)
                lineTo(22.2f, 8.2f)
                curveTo(22.6f, 8.2f, 23f, 8.6f, 23f, 9f)
                curveTo(23f, 9.4f, 22.6f, 9.8f, 22.2f, 9.8f)
                close()

                moveTo(22.2f, 13.8f)
                lineTo(19.8f, 13.8f)
                curveTo(19.400000000000002f, 13.8f, 19f, 13.5f, 19f, 13f)
                curveTo(19f, 12.5f, 19.4f, 12.2f, 19.8f, 12.2f)
                lineTo(22.2f, 12.2f)
                curveTo(22.599999999999998f, 12.2f, 23f, 12.5f, 23f, 13f)
                curveTo(23f, 13.5f, 22.6f, 13.8f, 22.2f, 13.8f)
            }
        }
        return _song!!
    }

private var _song: ImageVector? = null

@Preview
@Composable
fun Prev() {
    Icon(imageVector = Icons.Filled.Song, contentDescription = null)
}