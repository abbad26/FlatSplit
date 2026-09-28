package com.techhub.flatsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.presentation.MainScreen
import com.techhub.flatsplit.presentation.RoomSelectionViewModel
import com.techhub.flatsplit.presentation.auth.AuthViewModel
import com.techhub.flatsplit.presentation.auth.LoginScreen
import com.techhub.flatsplit.presentation.auth.StartDestination
import com.techhub.flatsplit.presentation.home.HomeScreen
import com.techhub.flatsplit.presentation.home.HomeViewModel
import com.techhub.flatsplit.presentation.settleup.SettleUpViewModel
import com.techhub.flatsplit.ui.theme.FlatSplitTheme
import com.techhub.flatsplit.ui.theme.rememberWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val window = rememberWindowSizeClass()
            FlatSplitTheme(window) {


                val viewModel: AuthViewModel = hiltViewModel()
                val homeViewModel: HomeViewModel = hiltViewModel()
                val roomSelectionViewModel: RoomSelectionViewModel = hiltViewModel()
                val settleUpViewModel: SettleUpViewModel = hiltViewModel()

                when (
                    val destination = viewModel.startDestination.collectAsStateWithLifecycle().value
                ) {
                    StartDestination.Login -> {
                        LoginScreen(
                            viewModel = viewModel
                        )
                    }

                    StartDestination.Home -> {

                        MainScreen(
                            authViewModel = viewModel,
                            homeViewModel = homeViewModel,
                            roomSelectionViewModel = roomSelectionViewModel,
                            settleUpViewModel = settleUpViewModel
                        )
                    }

                    else -> {

                    }
                }

            }
        }
    }
}
