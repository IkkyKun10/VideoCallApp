package com.riezki.mystreamsdk.video

import io.getstream.video.android.core.Call

/**
 * @author riezky maisyar
 */

data class VideoCallState(
    val call: Call,
    val error: String? = null,
    val callState: CallState? = null
)

enum class CallState{
    JOINING,
    ACTIVE,
    ENDED
}