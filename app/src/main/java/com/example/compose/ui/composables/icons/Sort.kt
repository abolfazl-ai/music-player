package com.example.compose.ui.composables.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Sort: ImageVector
    get() {
        if (_sort != null) {
            return _sort!!
        }
        _sort = materialIcon(name = "Filled.Artist") {
            materialPath {
                moveTo(10.2F,11F)
                lineTo(3.8F,11F)
                curveTo(3.3F,11F,3F,10.7F,3F,10.2F)
                lineTo(3F,8.8F)
                curveTo(3F,8.3F,3.3F,8F,3.8F,8F)
                lineTo(10.3F,8F)
                curveTo(10.7F,8F,11F,8.3F,11F,8.8F)
                lineTo(11F,10.3F)
                curveTo(11F,10.7F,10.7F,11F,10.2F,11F)
                close()

                moveTo(8.8F,6F)
                lineTo(3.8000000000000007F,6F)
                curveTo(3.3F,6F,3F,5.7F,3F,5.2F)
                lineTo(3F,3.8F)
                curveTo(3F,3.3F,3.3F,3F,3.8F,3F)
                lineTo(8.8F,3F)
                curveTo(9.200000000000001F,3F,9.600000000000001F,3.3F,9.600000000000001F,3.8F)
                lineTo(9.600000000000001F,5.3F)
                curveTo(9.5F,5.7F,9.2F,6F,8.8F,6F)
                close()

                moveTo(11.8F,16F)
                lineTo(3.8000000000000007F,16F)
                curveTo(3.3F,16F,3F,15.7F,3F,15.2F)
                lineTo(3F,13.7F)
                curveTo(3F,13.3F,3.3F,13F,3.8F,13F)
                lineTo(11.8F,13F)
                curveTo(12.200000000000001F,13F,12.600000000000001F,13.3F,12.600000000000001F,13.8F)
                lineTo(12.600000000000001F,15.3F)
                curveTo(12.5F,15.7F,12.2F,16F,11.8F,16F)
                close()

                moveTo(13.3F,21F)
                lineTo(3.8F,21F)
                curveTo(3.3F,21F,3F,20.7F,3F,20.2F)
                lineTo(3F,18.7F)
                curveTo(3F,18.3F,3.3F,18F,3.8F,18F)
                lineTo(13.3F,18F)
                curveTo(13.700000000000001F,18F,14.100000000000001F,18.3F,14.100000000000001F,18.8F)
                lineTo(14.100000000000001F,20.3F)
                curveTo(14F,20.7F,13.7F,21F,13.3F,21F)
                close()

                moveTo(20.5F,18F)
                lineTo(19.8F,18F)
                lineTo(19.8F,6F)
                lineTo(20.5F,6F)
                curveTo(20.7F,6F,20.8F,5.8F,20.7F,5.6F)
                lineTo(18.7F,3.1999999999999997F)
                curveTo(18.599999999999998F,3.0999999999999996F,18.4F,3.0999999999999996F,18.3F,3.1999999999999997F)
                lineTo(16.3F,5.6F)
                curveTo(16.2F,5.8F,16.3F,6F,16.5F,6F)
                lineTo(17.2F,6F)
                lineTo(17.2F,18F)
                lineTo(16.5F,18F)
                curveTo(16.3F,18F,16.2F,18.2F,16.3F,18.4F)
                lineTo(18.3F,20.799999999999997F)
                curveTo(18.400000000000002F,20.9F,18.6F,20.9F,18.7F,20.799999999999997F)
                lineTo(20.7F,18.4F)
                curveTo(20.8F,18.2F,20.7F,18F,20.5F,18F)
                close()
            }
        }
        return _sort!!
    }

private var _sort: ImageVector? = null