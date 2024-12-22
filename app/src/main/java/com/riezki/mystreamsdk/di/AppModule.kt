package com.riezki.mystreamsdk.di

import android.app.Application
import com.riezki.mystreamsdk.MyApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.getstream.video.android.core.StreamVideo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideVideoStream(application: Application) : StreamVideo {
        val app = application as MyApp
        return app.client as StreamVideo
    }
}