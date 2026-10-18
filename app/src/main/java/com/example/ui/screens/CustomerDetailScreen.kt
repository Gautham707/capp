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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.TransactionEntity
import com.example.ui.components.AddDueDialog
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.DueActionCards
import com.example.ui.components.EditCustomerDialog
import com.example.ui.components.EditTransactionDialog
import com.example.ui.components.HeroOutstandingCard
import com.example.ui.components.RecordPaymentDialog
import com.example.ui.components.formatCurrency
import com.example.ui.components.formatTime
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BluePrimaryLight
import com.example.ui.theme.RoseDue
import com.example.ui.theme.TealSuccess
import com.example.viewmodel.MyDueViewModel
import com.example.viewmodel.Screen

@Composable
fun CustomerDetailScreen(
    customerId: Long,
    viewModel: MyDueViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    LaunchedEffect(customerId) {
        viewModel.selectCustomer(customerId)
    }

    val allSummaries by viewModel.allSummaries.collectAsStateWithLifecycle()
    val transactions by viewModel.selectedCustomerTransactions.collectAsStateWithLifecycle()

    val summary = allSummaries.find { it.customer.id == customerId }

    var showAddDueDialog by remember { mutableStateOf(false) }
    var showRecordPaymentDialog by remember { mutableStateOf(false) }
    var showEditCustomerDialog by remember { mutableStateOf(false) }
    var showDeleteCustomerDialog by remember { mutableStateOf(false) }
    var transactionToEdit by remember { mutableStateOf<TransactionEntity?>(null) }
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }

    if (summary == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Customer not found", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = { viewModel.navigateBack() }) {
                    Text("Back to Home")
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // RETRO TOP NAV BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("detail_top_nav"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                            .clickable { viewModel.navigateBack() }
                            .testTag("detail_back_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = summary.customer.name,
                            fontSize = 17.sp,
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("detail_customer_name")
                        )
                        Text(
                            text = if (summary.customer.phone.isNotBlank()) "[TEL: ${summary.customer.phone}]" else "[NO PHONE RECORD]",
                            fontSize = 11.sp,
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag("detail_customer_phone")
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
                            .clickable { showEditCustomerDialog = true }
                            .testTag("detail_edit_customer_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Customer", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(17.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, RoseDue, RoundedCornerShape(6.dp))
                            .clickable { showDeleteCustomerDialog = true }
                            .testTag("detail_delete_customer_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Customer", tint = RoseDue, modifier = Modifier.size(17.dp))
                    }
                }
            }
        }

        // HERO BALANCE CARD
        item {
            HeroOutstandingCard(totalAmount = summary.balance)
        }

        // ACTION BUTTONS ROW (Add Due / Record Payment)
        item {
            DueActionCards(
                onAddDueClick = { showAddDueDialog = true },
                onRecordPaymentClick = { showRecordPaymentDialog = true },
                addDueTestTag = "detail_add_credit_btn",
                recordPaymentTestTag = "detail_record_payment_btn"
            )
        }

        // TRANSACTION HISTORY SECTION
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TRANSACTION HISTORY",
                    fontSize = 14.sp,
                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "[${transactions.size} ENTRIES]",
                    fontSize = 11.sp,
                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (transactions.isEmpty()) {
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
                            text = "[NO TRANSACTIONS RECORDED]",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(transactions, key = { it.id }) { tx ->
                val isCredit = tx.type == "credit"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transaction_item_${tx.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isCredit) RoseDue.copy(alpha = 0.15f)
                                        else TealSuccess.copy(alpha = 0.15f)
                                    )
                                    .border(
                                        1.dp,
                                        if (isCredit) RoseDue else TealSuccess,
                                        RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isCredit) "+D" else "+P",
                                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCredit) RoseDue else TealSuccess
                                )
                            }

                            Column {
                                Text(
                                    text = if (isCredit) "DUE ADDED" else "PAYMENT RECEIVED",
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = formatTime(tx.timestamp),
                                    fontSize = 10.5.sp,
                                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${if (isCredit) "+" else "-"}${formatCurrency(tx.amount)}",
                                fontSize = 15.sp,
                                fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = if (isCredit) RoseDue else TealSuccess
                            )

                            IconButton(
                                onClick = { transactionToEdit = tx },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit tx",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            IconButton(
                                onClick = { transactionToDelete = tx },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete tx",
                                    tint = RoseDue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showAddDueDialog) {
        AddDueDialog(
            existingCustomers = allSummaries,
            initialCustomerName = summary.customer.name,
            onDismiss = { showAddDueDialog = false },
            onSave = { name, phone, amount, note ->
                viewModel.addDue(name, phone, amount, note) {
                    showAddDueDialog = false
                }
            }
        )
    }

    if (showRecordPaymentDialog) {
        RecordPaymentDialog(
            customerName = summary.customer.name,
            currentBalance = summary.balance,
            onDismiss = { showRecordPaymentDialog = false },
            onSave = { amount, note ->
                viewModel.recordPayment(summary.customer.id, amount, note) {
                    showRecordPaymentDialog = false
                }
            }
        )
    }

    if (showEditCustomerDialog) {
        EditCustomerDialog(
            customer = summary.customer,
            onDismiss = { showEditCustomerDialog = false },
            onSave = { updated ->
                viewModel.updateCustomer(updated) {
                    showEditCustomerDialog = false
                }
            }
        )
    }

    if (showDeleteCustomerDialog) {
        ConfirmDeleteDialog(
            title = "Delete Customer",
            message = "Are you sure you want to delete ${summary.customer.name} and all associated credit/payment history? This action cannot be undone.",
            onDismiss = { showDeleteCustomerDialog = false },
            onConfirm = {
                viewModel.deleteCustomer(summary.customer.id) {
                    showDeleteCustomerDialog = false
                    viewModel.navigateTo(Screen.Home)
                }
            }
        )
    }

    if (transactionToEdit != null) {
        EditTransactionDialog(
            transaction = transactionToEdit!!,
            onDismiss = { transactionToEdit = null },
            onSave = { updated ->
                viewModel.updateTransaction(updated) {
                    transactionToEdit = null
                }
            }
        )
    }

    if (transactionToDelete != null) {
        ConfirmDeleteDialog(
            title = "Delete Transaction",
            message = "Remove this transaction record of ₹${transactionToDelete!!.amount.toInt()}? The customer balance will be automatically adjusted.",
            onDismiss = { transactionToDelete = null },
            onConfirm = {
                viewModel.deleteTransaction(transactionToDelete!!.id) {
                    transactionToDelete = null
                }
            }
        )
    }
}
