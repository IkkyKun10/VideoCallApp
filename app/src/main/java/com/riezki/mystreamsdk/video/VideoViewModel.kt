package com.riezki.mystreamsdk.video

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author riezky maisyar
 */

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoClient: StreamVideo
) : ViewModel() {

    var state by mutableStateOf(
        VideoCallState(
            call = videoClient.call("default", "main-room")
        )
    )
        private set

    fun onAction(action: VideoCallAction) {
        when (action) {
            VideoCallAction.JoinCall -> joinCall()
            VideoCallAction.Leave -> {
                state.call.leave()
                videoClient.logOut()
                state = state.copy(callState = CallState.ENDED)
            }
        }
    }

    private fun joinCall() {
        if (state.callState == CallState.ACTIVE) {
            return
        }

        viewModelScope.launch {
            state = state.copy(callState = CallState.JOINING)

            val shouldCreateCall = videoClient
                .queryCalls(filters = emptyMap())
                .getOrThrow()
                .calls
                .isEmpty()

            state.call.join(
                create = shouldCreateCall,
            ).onSuccess {
                state = state.copy(
                    callState = CallState.ACTIVE,
                    error = null
                )
            }.onError {
                state = state.copy(
                    error = it.message,
                    callState = null
                )
            }
        }
    }
}