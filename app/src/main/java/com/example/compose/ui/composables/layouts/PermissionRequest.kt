package com.example.compose.ui.composables.layouts

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun PermissionRequest(
    modifier: Modifier = Modifier,
    requestScreen: @Composable (request: () -> Unit) -> Unit,
    content: @Composable () -> Unit
) =
    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        val storagePermissionState =
            rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

        PermissionRequired(
            permissionState = storagePermissionState,
            permissionNotGrantedContent = { requestScreen { storagePermissionState.launchPermissionRequest() } },
            permissionNotAvailableContent = { requestScreen { storagePermissionState.launchPermissionRequest() } },
            content = content
        )
    }

@Composable
fun RequestScreen(request: () -> Unit) = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Button(
        modifier = Modifier.height(44.dp),
        onClick = request
    ) {
        Text("Grant Storage Permission")
    }
}