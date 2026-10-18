package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CustomerWithSummary
import com.example.ui.components.AddDueDialog
import com.example.ui.components.CustomerCard
import com.example.ui.components.RecordPaymentDialog
import com.example.ui.components.formatCurrency
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BluePrimaryLight
import com.example.ui.theme.RoseDue
import com.example.ui.theme.TealSuccess
import com.example.viewmodel.MyDueViewModel
import com.example.viewmodel.Screen

@Composable
fun CreditLedgerScreen(
    viewModel: MyDueViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val totalOutstanding by viewModel.totalOutstanding.collectAsStateWithLifecycle()
    val totalCollected by viewModel.totalCollected.collectAsStateWithLifecycle()
    val pendingCount by viewModel.pendingCount.collectAsStateWithLifecycle()
    val settledCount by viewModel.settledCount.collectAsStateWithLifecycle()
    val activeFilter by viewModel.ledgerFilter.collectAsStateWithLifecycle()
    val filteredCustomers by viewModel.filteredLedgerCustomers.collectAsStateWithLifecycle()
    val allSummaries by viewModel.allSummaries.collectAsStateWithLifecycle()

    var showAddDueDialog by remember { mutableStateOf(false) }
    var selectedForCredit by remember { mutableStateOf<String?>(null) }
    var selectedForPayment by remember { mutableStateOf<CustomerWithSummary?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // RETRO LEDGER HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ledger_header"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = "Ledger",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "CREDIT LEDGER",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "// MASTER ACCOUNT FILE //",
                            fontSize = 10.5.sp,
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = {
                        selectedForCredit = ""
                        showAddDueDialog = true
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.testTag("ledger_add_due_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(15.dp))
                    Text(
                        text = "+ DUE",
                        fontSize = 12.sp,
                        fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // DUAL METRIC STATS - RETRO TACTILE CARDS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Due Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.setLedgerFilter("due") }
                        .testTag("ledger_stat_due"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "[TOTAL DUE]",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseDue
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatCurrency(totalOutstanding),
                            fontSize = 20.sp,
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = RoseDue
                        )
                        Text(
                            text = "$pendingCount accounts",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Total Collected Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.setLedgerFilter("settled") }
                        .testTag("ledger_stat_collected"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "[COLLECTED]",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealSuccess
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatCurrency(totalCollected),
                            fontSize = 20.sp,
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TealSuccess
                        )
                        Text(
                            text = "$settledCount settled",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // FILTER PILLS (All, Pending, Today, Settled) - RETRO BRACKET CHIPS
        item {
            val filters = listOf(
                "all" to "ALL: ${allSummaries.size}",
                "due" to "PENDING: $pendingCount",
                "today" to "TODAY",
                "settled" to "SETTLED: $settledCount"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("filter_pills_row"),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { (key, label) ->
                    val isSelected = activeFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { viewModel.setLedgerFilter(key) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("chip_$key"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "[$label]",
                            fontSize = 10.5.sp,
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // CUSTOMER ACCOUNTS SECTION
        item {
            Text(
                text = "CUSTOMER ACCOUNTS",
                fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (filteredCustomers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "[NO ACCOUNTS IN '$activeFilter']",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(
                items = filteredCustomers,
                key = { it.customer.id },
                contentType = { "customer_card" }
            ) { item ->
                CustomerCard(
                    summary = item,
                    onClick = { viewModel.navigateTo(Screen.Detail(item.customer.id)) },
                    onAddCredit = {
                        selectedForCredit = item.customer.name
                        showAddDueDialog = true
                    },
                    onPay = {
                        selectedForPayment = item
                    }
                )
            }
        }



        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }

    if (showAddDueDialog) {
        AddDueDialog(
            existingCustomers = allSummaries,
            initialCustomerName = selectedForCredit ?: "",
            onDismiss = { showAddDueDialog = false },
            onSave = { name, phone, amount, note ->
                viewModel.addDue(name, phone, amount, note) {
                    showAddDueDialog = false
                }
            }
        )
    }

    if (selectedForPayment != null) {
        val cust = selectedForPayment!!
        RecordPaymentDialog(
            customerName = cust.customer.name,
            currentBalance = cust.balance,
            onDismiss = { selectedForPayment = null },
            onSave = { amount, note ->
                viewModel.recordPayment(cust.customer.id, amount, note) {
                    selectedForPayment = null
                }
            }
        )
    }
}
