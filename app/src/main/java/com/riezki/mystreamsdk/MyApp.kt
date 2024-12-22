package com.riezki.mystreamsdk

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType

/**
 * @author riezky maisyar
 */

@HiltAndroidApp
class MyApp : Application() {

    private var currentName: String? = null
    var client: StreamVideo? = null

    fun initStreamVideo(name: String) {
        if (client == null || name != currentName) {
            StreamVideo.removeClient()
            client = null
            currentName = name
            client = StreamVideoBuilder(
                context = this,
                apiKey = "smk59v6a9n4g",
                user = User(
                    id = name,
                    name = name,
                    type = UserType.Guest
                )
            ).build()
        }
    }

    fun closeVideoCall() {
        client?.logOut()
        client = null
    }
}