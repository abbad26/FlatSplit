package com.techhub.flatsplit.presentation.settleup

import com.techhub.flatsplit.domain.model.User
import com.techhub.flatsplit.domain.model.settlement.Expense
import com.techhub.flatsplit.domain.model.settlement.MemberBalance
import com.techhub.flatsplit.domain.model.settlement.Settlement
import com.techhub.flatsplit.domain.model.settlement.SettlementSuggestion

data class SettleUpUiState(
    val isLoading: Boolean = false,
    val expenses: List<Expense> = emptyList(),
    val users: List<User> = emptyList(),
    val settlements: List<Settlement> = emptyList(),
    val balances: List<MemberBalance> = emptyList(),
    val suggestions: List<SettlementSuggestion> = emptyList(),
    val error: String? = null
)