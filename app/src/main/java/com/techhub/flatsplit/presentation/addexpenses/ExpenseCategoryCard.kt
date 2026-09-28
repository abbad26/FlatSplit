package com.techhub.flatsplit.presentation.addexpenses

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.techhub.flatsplit.ui.theme.BorderSubtle
import com.techhub.flatsplit.ui.theme.TextSecondary


@Composable
fun ExpenseCategoryGrid(
    categories: List<ExpenseCategory>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
){

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.chunked(3).forEach { rowCategories ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                rowCategories.forEach { category ->

                    ExpenseCategoryCard(
                        category = category,
                        selected = selectedCategory == category.name,
                        onClick = {
                            onCategorySelected(category.name)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                repeat(3 - rowCategories.size){
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

        }
    }
}
@Composable
private fun ExpenseCategoryCard(
    category: ExpenseCategory,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){

    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else{
                MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else{
                BorderSubtle
            }
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = category.color,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}