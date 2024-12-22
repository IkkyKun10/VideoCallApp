package com.riezki.mystreamsdk.video

/**
 * @author riezky maisyar
 */

sealed interface VideoCallAction {
    data object JoinCall : VideoCallAction
    data object Leave : VideoCallAction
}