package com.riezki.mystreamsdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.riezki.mystreamsdk.connect.ConnectScreen
import com.riezki.mystreamsdk.connect.ConnectViewModel
import com.riezki.mystreamsdk.ui.theme.MyStreamSDKTheme
import com.riezki.mystreamsdk.video.CallState
import com.riezki.mystreamsdk.video.VideoCallScreen
import com.riezki.mystreamsdk.video.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.video.android.compose.theme.VideoTheme
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStreamSDKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ConnectRoute,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<ConnectRoute> {
                            val viewModel = hiltViewModel<ConnectViewModel>()

                            val state = viewModel.state

                            LaunchedEffect(state.isConnected) {
                                if (state.isConnected) {
                                    navController.navigate(VideoCallRoute) {
                                        popUpTo(ConnectRoute) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                            ConnectScreen(
                                state = state,
                                onAction = viewModel::onAction
                            )
                        }
                        composable<VideoCallRoute> {
                            val viewModel = hiltViewModel<VideoViewModel>()

                            val state = viewModel.state

                            LaunchedEffect(state?.callState) {
                                if (state?.callState == CallState.ENDED) {
                                    navController.navigate(ConnectRoute) {
                                        popUpTo(VideoCallRoute) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                            VideoTheme {
                               state?.let {
                                   VideoCallScreen(
                                       state = state,
                                       onAction = viewModel::onAction
                                   )
                               }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Serializable
data object ConnectRoute

@Serializable
data object VideoCallRoute