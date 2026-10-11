package com.techhub.flatsplit.presentation.guide


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techhub.flatsplit.ui.theme.AppTheme
import com.techhub.flatsplit.ui.theme.BgDark
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.Payments

@Composable
fun AppGuideScreen(
    onBack: () -> Unit,
    onGoToFlats: () -> Unit,
    onGoToSettleUp: () -> Unit,
    onGoToProfile: () -> Unit,
) {
    val dimens = AppTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(top = dimens.large)
    ) {
        GuideTopBar(onBack = onBack)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = dimens.large,
                end = dimens.large,
                bottom = dimens.large * 2f
            ),
            verticalArrangement = Arrangement.spacedBy(dimens.large)
        ) {
            item { HeroSection() }

            item {
                QuickStartSection(
                    onGoToFlats = onGoToFlats,
                    onGoToSettleUp = onGoToSettleUp,
                    onGoToProfile = onGoToProfile
                )
            }

            item { GuideSectionHeading("Create or join your flat") }
            item {
                GuideStepCard(
                    number = 1,
                    icon = Icons.Outlined.GroupAdd,
                    title = "Create a flat",
                    body = "Open the Flats screen from the bottom navigation and tap Create Flat. " +
                            "Enter the flat name to generate an invite code, then share the invite code with your flatmates."
                )
            }
            item {
                GuideStepCard(
                    number = 2,
                    icon = Icons.Outlined.GroupAdd,
                    title = "Join a flat",
                    body = "Open the Flats screen, tap Join Flat, and enter the invite code " +
                            "shared by a flatmate. Use the flat switcher on Home or Settle Up " +
                            "to switch between your flats."
                )
            }

            item { GuideSectionHeading("Add a shared expense") }
            item {
                GuideStepCard(
                    number = 3,
                    icon = Icons.AutoMirrored.Outlined.ReceiptLong,
                    title = "Record what was spent",
                    body = "From Home, tap the + floating action button to open Add Expense. " +
                            "Enter the title, amount, category, who paid, and select which flatmates to split the expense between."
                )
            }
            item { ExpenseExampleCallout() }

            item { GuideSectionHeading("Understand your balance") }
            item { BalanceExplainerCard() }

            item { GuideSectionHeading("Settle up") }
            item {
                GuideStepCard(
                    number = 4,
                    icon = Icons.Outlined.Payments,
                    title = "Review the suggestions",
                    body = "Open Settle Up to view your overall balance and review Settlement Suggestions, " +
                            "which show who needs to pay whom and how much to settle up."
                )
            }
            item {
                GuideStepCard(
                    number = 5,
                    icon = Icons.Outlined.Payments,
                    title = "Record how you paid",
                    body = "Tap Settle or Pay now on a suggestion, choose Online / UPI or Cash, and confirm " +
                            "your payment report. Make any UPI transfer in your own payment app first—FlatSplit " +
                            "records your report but does not initiate or verify bank transactions."
                )
            }
            item { NotTransferCallout() }

            item { GuideSectionHeading("Payment confirmation") }
            item { PaymentFlowSection() }

            item { GuideSectionHeading("Payment history") }
            item {
                GuideStepCard(
                    number = 6,
                    icon = Icons.AutoMirrored.Outlined.ReceiptLong,
                    title = "Review recent recorded payments",
                    body = "On Settle Up, tap History → next to Settlement Suggestions. The history " +
                            "shows recorded settlement details for the selected flat over the last " +
                            "30 days, including amount, people, method, date, and status (Pending, Confirmed, or Not received)."
                )
            }

            item { GuideSectionHeading("Your profile") }
            item {
                GuideStepCard(
                    number = 7,
                    icon = Icons.Outlined.AccountCircle,
                    title = "Manage your account",
                    body = "In Profile, view your account details, set or edit your UPI ID, " +
                            "manage flat members and settings, share feedback, or log out."
                )
            }

            item { TipsSection() }
            item { FaqSection() }
            item { BottomCta(onDone = onBack) }
        }
    }
}
