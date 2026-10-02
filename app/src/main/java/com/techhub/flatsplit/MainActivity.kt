package com.techhub.flatsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techhub.flatsplit.presentation.MainScreen
import com.techhub.flatsplit.presentation.RoomSelectionViewModel
import com.techhub.flatsplit.presentation.SplashScreen
import com.techhub.flatsplit.presentation.auth.AuthViewModel
import com.techhub.flatsplit.presentation.auth.LoginScreen
import com.techhub.flatsplit.presentation.auth.StartDestination
import com.techhub.flatsplit.presentation.home.HomeViewModel
import com.techhub.flatsplit.presentation.settleup.SettleUpViewModel
import com.techhub.flatsplit.ui.theme.FlatSplitTheme
import com.techhub.flatsplit.ui.theme.rememberWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val window = rememberWindowSizeClass()

            FlatSplitTheme(window) {

                val authViewModel: AuthViewModel =
                    hiltViewModel()

                val homeViewModel: HomeViewModel =
                    hiltViewModel()

                val roomSelectionViewModel:
                        RoomSelectionViewModel = hiltViewModel()

                val settleUpViewModel:
                        SettleUpViewModel = hiltViewModel()

                val destination by authViewModel.startDestination.collectAsStateWithLifecycle()

                var splashAnimationFinished by remember {
                    mutableStateOf(false)
                }

                when {

                    // Splash animation is still running
                    !splashAnimationFinished -> {

                        SplashScreen(
                            onAnimationFinished = {
                                splashAnimationFinished = true
                            }
                        )
                    }

                    // Animation finished, but auth is still loading
                    destination == StartDestination.Loading -> {

                        SplashScreen(
                            onAnimationFinished = { }
                        )
                    }

                    // Both are ready → Login
                    destination == StartDestination.Login -> {

                        LoginScreen(
                            viewModel = authViewModel
                        )
                    }

                    // Both are ready → Home
                    destination == StartDestination.Home -> {

                        MainScreen(
                            authViewModel = authViewModel,
                            homeViewModel = homeViewModel,
                            roomSelectionViewModel =
                                roomSelectionViewModel,
                            settleUpViewModel =
                                settleUpViewModel
                        )
                    }
                }
            }
        }
    }
}