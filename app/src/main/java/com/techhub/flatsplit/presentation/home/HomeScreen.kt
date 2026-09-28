package com.techhub.flatsplit.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.presentation.RoomSelectionViewModel
import com.techhub.flatsplit.presentation.components.AppError
import com.techhub.flatsplit.presentation.shimmer.HomeShimmer
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    roomSelectionViewModel: RoomSelectionViewModel,
    homeViewModel: HomeViewModel,
    onAddExpense: (String) -> Unit,
    onCreateFlat: () -> Unit,
    onJoinFlat: () -> Unit
) {

    val dimens = AppTheme.dimensions

    val roomState by roomSelectionViewModel.uiState.collectAsState()
    val homeState by homeViewModel.uiState.collectAsState()

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    // Load all flats of the logged-in user
    LaunchedEffect(currentUserId) {
        currentUserId?.let { userId ->
            roomSelectionViewModel.loadRooms(userId)
        }
    }

    // Load expenses whenever selected flat changes
    LaunchedEffect(
        roomState.selectedRoom?.id,
        currentUserId
    ) {
        val room = roomState.selectedRoom
        val userId = currentUserId

        if (room != null && userId != null) {
            homeViewModel.loadRoomData(
                room = room,
                currentUserId = userId
            )
        }
    }

    // userId -> user name
    val userNameMap = homeState.users.associateBy(
        keySelector = { it.id },
        valueTransform = { it.name }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    roomState.selectedRoom?.id?.let { roomId ->
                        onAddExpense(roomId)
                    }
                },
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp,
                    bottomStart = 30.dp
                ),
                containerColor = Color(0xFFFD7D0C)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Expense"
                )
            }
        }
    ) { paddingValues ->


        when {
            roomState.isLoading || homeState.isLoading -> {
                HomeShimmer(
                    modifier = Modifier.padding(paddingValues)
                )
            }
            roomState.error != null -> {

                AppError(
                    message = roomState.error!!,
                    modifier = Modifier.fillMaxSize()
                        .padding(paddingValues)
                )
            }
            homeState.error != null -> {

                AppError(
                    message = homeState.error!!,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            roomState.selectedRoom == null -> {

                EmptyHomeState(
                    onCreateFlat = { onCreateFlat() },
                    onJoinFlat = { onJoinFlat() }
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BgDark)
                        .padding(paddingValues)
                        .padding(horizontal = dimens.large)
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(dimens.medium)
                    ) {

                        item {
                            HomeWelcomeHeader(
                                name = userNameMap[currentUserId] ?: "User"
                            )
                        }

                        item {
                            FlatSwitcherDropdown(
                                rooms = roomState.rooms,
                                selectedRoom = roomState.selectedRoom,
                                onRoomSelected = { room ->
                                    roomSelectionViewModel.selectRoom(room)
                                }
                            )
                        }

                        item {
                            BalanceSummaryCard(
                                totalSpent = homeState.totalSpent,
                                yourPaid = homeState.yourPaid,
                                youOwe = if (homeState.currentBalance < 0)
                                    -homeState.currentBalance
                                else
                                    0L,
                                youGet = if (homeState.currentBalance > 0)
                                    homeState.currentBalance
                                else
                                    0L,
                                memberCount = roomState.selectedRoom?.memberIds?.size ?: 0
                            )
                        }

                        item {
                            Text(
                                text = "RECENT ACTIVITY",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }

                        items(
                            items = homeState.expenses,
                            key = { expense -> expense.id }
                        ) { expense ->

                            ExpenseActivityCard(
                                icon = categoryIcon(expense.category),
                                category = expense.category,
                                name = userNameMap[expense.paidBy] ?: "Unknown",
                                timestamp = expense.createdAt,
                                amountPaise = expense.amount
                            )
                        }

                        item {
                            Spacer(
                                modifier = Modifier.height(dimens.large)
                            )
                        }
                    }
                }

            }
        }
    }
}

private fun categoryIcon(category: String): ImageVector {

    return when (category.lowercase()) {

        "grocery",
        "groceries" -> Icons.Outlined.ShoppingCart

        "electricity",
        "bills" -> Icons.Outlined.Lightbulb

        "internet" -> Icons.Outlined.Wifi

        else -> Icons.Outlined.ShoppingCart
    }
}