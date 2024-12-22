package com.riezki.mystreamsdk.video

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.getstream.video.android.compose.permission.rememberCallPermissionsState
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.actions.DefaultOnCallActionHandler
import io.getstream.video.android.core.call.state.LeaveCall

/**
 * @author riezky maisyar
 */

@Composable
fun VideoCallScreen(
    state: VideoCallState,
    onAction: (VideoCallAction) -> Unit
) {
    when {
        state.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        state.callState == CallState.JOINING -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(text = "Joining...")
            }
        }

        else -> {
            val basePermissions = listOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO
            )

            val bluetoothPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                listOf(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                emptyList()
            }

            val notifPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                emptyList()
            }

            val context = LocalContext.current

            CallContent(
                call = state.call,
                modifier = Modifier
                    .fillMaxSize(),
                permissions = rememberCallPermissionsState(
                    call = state.call,
                    permissions = basePermissions + bluetoothPermission + notifPermission,
                    onPermissionsResult = { perms ->
                        if (perms.values.contains(false)) {
                            Toast.makeText(
                                context,
                                "Please grant all permissions to use this app.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            onAction(VideoCallAction.JoinCall)
                        }
                    }
                ),
                onCallAction = { action ->
                    if (action == LeaveCall) {
                        onAction(VideoCallAction.Leave)
                    }

                    DefaultOnCallActionHandler.onCallAction(state.call, action)
                },
                onBackPressed = {
                    onAction(VideoCallAction.Leave)
                }
            )
        }
    }
}