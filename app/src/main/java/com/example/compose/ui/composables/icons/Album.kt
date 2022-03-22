package com.example.compose.ui.composables.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.Album: ImageVector
    get() {
        if (_album != null) {
            return _album!!
        }
        _album = materialIcon(name = "Filled.Artist") {
            materialPath {
                moveTo(3.7F,4.2F)
                lineTo(2.6F,4.2F)
                curveTo(2.1F,4.2F,1.6F,4.6F,1.6F,5.2F)
                lineTo(1.6F,18.8F)
                curveTo(1.6F,19.4F,2F,19.8F,2.6F,19.8F)
                lineTo(3.7F,19.8F)
                lineTo(3.7F,4.2F)
                close()

                moveTo(15.6F,5.1F)
                lineTo(15.6F,6F)
                curveTo(17.2F,6F,18.8F,6.7F,19.9F,8F)
                curveTo(20.1F,8.2F,20.1F,8.5F,19.9F,8.7F)
                curveTo(19.8F,8.8F,19.7F,8.8F,19.6F,8.8F)
                curveTo(19.5F,8.8F,19.3F,8.7F,19.2F,8.6F)
                curveTo(18.3F,7.6F,17F,7F,15.6F,7F)
                lineTo(15.6F,7.8F)
                curveTo(16.7F,7.8F,17.8F,8.3F,18.5F,9.2F)
                curveTo(18.7F,9.4F,18.7F,9.7F,18.5F,9.9F)
                curveTo(18.4F,10F,18.3F,10F,18.2F,10F)
                curveTo(18.1F,10F,17.9F,9.9F,17.8F,9.8F)
                curveTo(17.2F,9.2F,16.5F,8.8F,15.6F,8.6F)
                lineTo(15.6F,10.2F)
                curveTo(16.5F,10.3F,17.2F,11F,17.2F,12F)
                curveTo(17.2F,12.9F,16.5F,13.7F,15.6F,13.8F)
                lineTo(15.6F,19F)
                curveTo(19.4F,18.9F,22.4F,15.8F,22.4F,12F)
                curveTo(22.4F,8.2F,19.3F,5.1F,15.6F,5.1F)
                close()

                moveTo(13.8F,4.2F)
                lineTo(4.8F,4.2F)
                lineTo(4.8F,19.8F)
                lineTo(13.8F,19.8F)
                curveTo(14.3F,19.8F,14.8F,19.4F,14.8F,18.8F)
                lineTo(14.8F,5.2F)
                curveTo(14.8F,4.6F,14.3F,4.2F,13.8F,4.2F)
                close()

                moveTo(11.9F,10.1F)
                lineTo(10.4F,10.1F)
                lineTo(10.4F,13.8F)
                curveTo(10.4F,14.6F,9.7F,15.3F,8.9F,15.3F)
                curveTo(8.1F,15.3F,7.4F,14.6F,7.4F,13.8F)
                curveTo(7.4F,13F,8.1F,12.3F,8.9F,12.3F)
                curveTo(9.2F,12.3F,9.4F,12.4F,9.6F,12.5F)
                lineTo(9.6F,8.7F)
                lineTo(11.8F,8.7F)
                lineTo(11.8F,10.1F)
                close()
            }
        }
        return _album!!
    }

private var _album: ImageVector? = null