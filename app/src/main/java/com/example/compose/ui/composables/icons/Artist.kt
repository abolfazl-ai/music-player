package com.example.compose.ui.composables.icons

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.translationMatrix

val Icons.Filled.Artist: ImageVector
    get() {
        if (_artist != null) {
            return _artist!!
        }
        _artist = materialIcon(name = "Filled.Artist") {
            materialPath {

                moveTo(7f, 9.5f)
                arcTo(
                    5f,
                    5f,
                    179f,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    x1 = 7f,
                    y1 = 9.6f
                );
                close()

                moveTo(15.5f, 15.8f);
                lineTo(8.5f, 15.8f);
                curveTo(6f, 15.8f, 4f, 17.8f, 4f, 20.3f);
                lineTo(4f, 22.3f);
                lineTo(20f, 22.3f);
                lineTo(20f, 20.3f);
                curveTo(20f, 17.7f, 18f, 15.8f, 15.5f, 15.8f);
                close()

                moveTo(20.7f, 7.8f);
                curveTo(20.7f, 7.8f, 20f, 7.8f, 19.6f, 7.8f);
                curveTo(19.4f, 7.8f, 19.3f, 7.7f, 19.2f, 7.5f);
                curveTo(18.3f, 4.3f, 15.4f, 2f, 12f, 2f);
                curveTo(8.6f, 2f, 5.7f, 4.3f, 4.8f, 7.5f);
                curveTo(4.7f, 7.6f, 4.6f, 7.8f, 4.4f, 7.8f);
                curveTo(4f, 7.8f, 3.3f, 7.8f, 3.3f, 7.8f);
                curveTo(2.8f, 7.8f, 2.5f, 8.2f, 2.5f, 8.6f);
                lineTo(2.5f, 11.5f);
                curveTo(2.5f, 12f, 2.9f, 12.3f, 3.3f, 12.3f);
                lineTo(4.7f, 12.3f);
                curveTo(5.2f, 12.3f, 5.5f, 11.9f, 5.5f, 11.5f);
                lineTo(5.5f, 9.7f);
                curveTo(5.5f, 6.4f, 7.9f, 3.4f, 11.1f, 3f);
                curveTo(15.1f, 2.5f, 18.5f, 5.6f, 18.5f, 9.4f);
                lineTo(18.5f, 11.3f);
                curveTo(18.5f, 11.8f, 18.9f, 12.1f, 19.3f, 12.1f);
                lineTo(20.7f, 12.1f);
                curveTo(21.2f, 12.1f, 21.5f, 11.7f, 21.5f, 11.3f);
                lineTo(21.5f, 8.6f);
                curveTo(21.5f, 8.1f, 21.1f, 7.8f, 20.7f, 7.8f);
                close()
            }
        }
        return _artist!!
    }

private var _artist: ImageVector? = null


@Preview
@Composable
fun Prev1() {
    Icon(imageVector = Icons.Filled.Artist, contentDescription = null)
}