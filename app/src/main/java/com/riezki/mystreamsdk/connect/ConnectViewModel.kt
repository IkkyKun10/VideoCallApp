package com.riezki.mystreamsdk.connect

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.riezki.mystreamsdk.MyApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author riezky maisyar
 */

@HiltViewModel
class ConnectViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    var state by mutableStateOf(ConnectState())
        private set

    fun onAction(action: ConnectAction) {
        when (action) {
            ConnectAction.OnConnectClick -> connectToRoom()
            is ConnectAction.OnNameChanged -> {
                state = state.copy(name = action.name)
            }
        }
    }

    private fun connectToRoom() {
        state = state.copy(errorMessage = null)
        if (state.name.isBlank()) {
            state = state.copy(
                errorMessage = "The name can't be empty"
            )
            return
        }

        (application as MyApp).initStreamVideo(state.name)

        state = state.copy(isConnected = true)
    }
}