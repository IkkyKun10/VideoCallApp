package com.riezki.mystreamsdk.connect

/**
 * @author riezky maisyar
 */

sealed interface ConnectAction {
    data class OnNameChanged(val name: String) : ConnectAction
    data object OnConnectClick : ConnectAction

}