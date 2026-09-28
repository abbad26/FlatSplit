package com.techhub.flatsplit.presentation

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techhub.flatsplit.presentation.auth.AuthViewModel
import com.techhub.flatsplit.presentation.auth.LoginScreen
import com.techhub.flatsplit.presentation.flatroom.FlatsScreen
import com.techhub.flatsplit.presentation.flatroom.OnFlatCreateScreen
import com.techhub.flatsplit.presentation.home.HomeScreen
import com.techhub.flatsplit.presentation.home.HomeViewModel
import com.techhub.flatsplit.presentation.manageflat.ManageFlatRoute
import com.techhub.flatsplit.presentation.profile.ProfileScreen
import com.techhub.flatsplit.presentation.settleup.SettleUpScreen
import com.techhub.flatsplit.presentation.settleup.SettleUpViewModel
import com.techhub.flatsplit.ui.theme.AccentGold
import com.techhub.flatsplit.ui.theme.SurfaceRaised
import com.techhub.flatsplit.ui.theme.TextFaint


const val FLAT_CREATED_ROUTE = "flat_created/{name}/{inviteCode}"
const val ADD_EXPENSE_ROUTE = "add_expense/{roomId}"
sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
        ) {

    object Home: Screen(
        route = "home",
        label = "Home",
        icon = Icons.Outlined.Home
    )

    object Flats: Screen(
        route = "flats",
        label = "Flats",
        icon = Icons.Outlined.Add
    )

    object SettleUp: Screen(
        route = "settle_up",
        label = "SettleUp",
        icon = Icons.Outlined.ReceiptLong
    )

    object Profile: Screen(
        route = "profile",
        label = "Profile",
        icon = Icons.Outlined.AccountCircle
    )

}


@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    roomSelectionViewModel: RoomSelectionViewModel,
    settleUpViewModel: SettleUpViewModel
){

    val navController = rememberNavController()
    val context = LocalContext.current

    val roomState by roomSelectionViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {

        authViewModel.event.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val screens = listOf(
        Screen.Home,
        Screen.Flats,
        Screen.SettleUp,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = screens.any {
        it.route == currentRoute
    }


    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SurfaceRaised,
                ) {

                    screens.forEach { screen ->

                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {

                                    popUpTo(Screen.Home.route) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },

                            icon = {

                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.label,
                                )
                            },

                            label = {
                                Text(
                                    text = screen.label,
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AccentGold,
                                selectedTextColor = AccentGold,
                                unselectedIconColor = TextFaint,
                                unselectedTextColor = TextFaint,
                                indicatorColor = AccentGold.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(
                if (showBottomBar) paddingValues
                else PaddingValues()
            )
        ) {

            composable(Screen.Home.route) {HomeScreen(
                roomSelectionViewModel = roomSelectionViewModel,
                homeViewModel = homeViewModel,
                onAddExpense = { roomId ->
                    navController.navigate(
                        "add_expense/$roomId"
                    )
                },
                onCreateFlat = { navController.navigate(Screen.Flats.route)},
                onJoinFlat = { navController.navigate(Screen.Flats.route)}
            )
            }

            composable(Screen.Flats.route) {
                FlatsScreen(
                    onFlatCreated = { room ->
                        navController.navigate(
                            "flat_created/${Uri.encode(room.name)}/${room.inviteCode}"
                        )
                    },
                    onFlatJoined = { navController.navigate(Screen.Home.route)}
                )
            }

            composable(
                route = FLAT_CREATED_ROUTE
            ) { backStackEntry ->

                val name =
                    backStackEntry.arguments?.getString("name")
                        ?: ""

                val inviteCode =
                    backStackEntry.arguments?.getString("inviteCode")
                        ?: ""

                OnFlatCreateScreen(
                    name = name,
                    inviteCode = inviteCode,
                    onBack = { navController.popBackStack()}
                )
            }

            composable(route = ADD_EXPENSE_ROUTE){ backStackEntry ->


                val roomId =
                    backStackEntry.arguments
                        ?.getString("roomId")
                        ?: return@composable
                val room = roomState.rooms.firstOrNull {
                    it.id == roomId
                }
                    ?: return@composable

                AddExpenseRoute(
                    room = room,
                    navController = navController
                )

            }

            composable(Screen.SettleUp.route) {
                SettleUpScreen(
                    viewModel = settleUpViewModel,
                    roomSelectionViewModel = roomSelectionViewModel
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onManageFlat = {
                        navController.navigate("manage_flat")
                    },
                    onLogout = {
                        authViewModel.logout()
                    }
                )
            }

            composable("manage_flat") {

                val room = roomState.selectedRoom

                if (room != null) {
                    ManageFlatRoute(
                        room = room,
                        navController = navController
                    )
                }
            }
        }
    }
}