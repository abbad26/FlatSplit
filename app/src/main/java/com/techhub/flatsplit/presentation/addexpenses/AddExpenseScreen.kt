package com.techhub.flatsplit.presentation.addexpenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.R
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import com.techhub.flatsplit.ui.theme.TextPrimary
import com.techhub.flatsplit.ui.theme.TextSecondary

@Composable
fun AddExpenseScreen(
    members: List<ExpenseMember>,
    currentUserId: String,
    isLoading: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onAddExpense:(
        title: String,
            amountPaise: Long,
            category: String,
            paidBy: String,
            splitBetween: List<String>
    ) -> Unit
){

    val dimens = AppTheme.dimensions

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    var validationError by remember {
        mutableStateOf<String?>(null)
    }

    var selectedCategory by remember {
        mutableStateOf("Grocery")
    }
    var paidBy by remember {
        mutableStateOf(currentUserId)
    }
    var selectedMembers by remember {
        mutableStateOf(setOf(currentUserId))
    }

    val categories = remember {
        listOf(
            ExpenseCategory(
                name = "Grocery",
                icon = Icons.Outlined.ShoppingCart,
                color = Color(0xFF4CAF50)
            ),
            ExpenseCategory(
                name = "Rent",
                icon = Icons.Outlined.Home,
                color = Color(0xFF42A5F5)
            ),
            ExpenseCategory(
                name = "Bills",
                icon = Icons.Outlined.ReceiptLong,
                color = Color(0xFFFFC107)
            ),
            ExpenseCategory(
                name = "Food",
                icon = Icons.Outlined.Fastfood,
                color = Color(0xFFFF7043)
            ),
            ExpenseCategory(
                name = "Transport",
                icon = Icons.Outlined.DirectionsCar,
                color = Color(0xFF26C6DA)
            ),
            ExpenseCategory(
                name = "Other",
                icon = Icons.Outlined.MoreHoriz,
                color = Color(0xFFAB47BC)
            )
        )
    }
    val amountPaise = parseAmountToPaise(amount)
    val perPersonPaise = if (selectedMembers.isNotEmpty()){
        amountPaise / selectedMembers.size
    } else{
        0L
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(BgDark)
    ) {

        Spacer(modifier = Modifier.height(dimens.large))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimens.large,
                    vertical = dimens.large
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {


            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Spacer(modifier = Modifier.width(dimens.medium))

            Text(
                text = "Add Expense",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = dimens.large,
                end = dimens.large,
                bottom = dimens.large
            ),
            verticalArrangement = Arrangement.spacedBy(dimens.medium)
        ) {

            item {
                ExpenseTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        validationError = null },
                    placeholder = "e.g. Groceries"
                )
            }

            item {
                ExpenseTextField(
                    value = amount,
                    onValueChange = {
                        if (it.all { char ->
                            char.isDigit() || char == '.'
                            }) {
                            amount = it
                            validationError = null
                        }
                    },
                    placeholder = "0"
                )
            }

            item {
                ExpenseCategoryGrid(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            item {
                PaidByDropdown(
                    members = members,
                    selectedUserId = paidBy,
                    onUserSelected = { paidBy = it}
                )
            }

            item {
                SplitMembersCard(
                    members = members,
                    selectedMembers = selectedMembers,
                    perPersonPaise = perPersonPaise,
                    onMemberChecked = { memberId ->

                        selectedMembers = if (memberId in selectedMembers){

                            if (selectedMembers.size == 1){
                                selectedMembers
                            } else{
                                selectedMembers - memberId
                            }
                        } else{
                            selectedMembers + memberId
                        }
                    }
                )
            }

            item {
                Text(
                    text = "${formatMoney(perPersonPaise)} per person",
                    modifier = Modifier.fillMaxWidth(),
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {

                validationError?.let { message ->

                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                errorMessage?.let { message ->

                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        when {
                            title.isBlank() -> {
                                validationError = "Please enter an expense title"
                            }
                            amountPaise <= 0L -> {
                                validationError = "Please enter a valid amount"
                            }
                            selectedMembers.isEmpty() -> {
                                validationError = "Select at least one member"
                            }
                            else -> {
                                validationError = null

                                onAddExpense(
                                    title.trim(),
                                    amountPaise,
                                    selectedCategory,
                                    paidBy,
                                    selectedMembers.toList()
                                )
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Add Expense",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


private fun parseAmountToPaise(
    amount: String
): Long {
    return try {
        amount
            .toBigDecimal()
            .movePointRight(2)
            .longValueExact()
    } catch (e: Exception) {
        0L
    }
}

private fun formatMoney(
    amountPaise: Long
): String {
    return "₹%,d".format(amountPaise / 100)
}