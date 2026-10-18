package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CustomerEntity
import com.example.data.CustomerWithSummary
import com.example.data.TransactionEntity
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.RoseDue
import com.example.ui.theme.TealSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDueDialog(
    existingCustomers: List<CustomerWithSummary>,
    initialCustomerName: String = "",
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String, amount: Double, note: String) -> Unit
) {
    var name by remember { mutableStateOf(initialCustomerName) }
    var phone by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    // Pre-fill phone if selecting an existing customer
    val matchingCustomer = existingCustomers.find { it.customer.name.equals(name.trim(), ignoreCase = true) }
    if (matchingCustomer != null && phone.isBlank() && matchingCustomer.customer.phone.isNotBlank()) {
        phone = matchingCustomer.customer.phone
    }

    val filteredSuggestions = existingCustomers.filter {
        name.isNotBlank() && it.customer.name.contains(name, ignoreCase = true)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header: Title and Close (X) icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ADD DUE / CREDIT",
                                fontSize = 17.sp,
                                fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Record credit purchase on ledger",
                                fontSize = 11.sp,
                                fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Field 1: Customer Name *
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "[CUSTOMER NAME] *",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded && filteredSuggestions.isNotEmpty(),
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    expanded = true
                                    errorText = null
                                },
                                placeholder = {
                                    Text(
                                        "e.g. Acme Supplies",
                                        fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("due_name_input")
                            )

                            ExposedDropdownMenu(
                                expanded = expanded && filteredSuggestions.isNotEmpty(),
                                onDismissRequest = { expanded = false }
                            ) {
                                filteredSuggestions.forEach { item ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(item.customer.name, fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
                                                if (item.customer.phone.isNotBlank()) {
                                                    Text(
                                                        item.customer.phone,
                                                        fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            name = item.customer.name
                                            phone = item.customer.phone
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Field 2: Phone Number (Optional)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "[PHONE NUMBER]",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            placeholder = {
                                Text(
                                    "e.g. +91 98765 43210",
                                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("due_phone_input")
                        )
                    }

                    // Field 3: Credit Amount (₹) *
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "[CREDIT AMOUNT (₹)] *",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = amountText,
                            onValueChange = {
                                amountText = it
                                errorText = null
                            },
                            placeholder = {
                                Text(
                                    "e.g. 500",
                                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("due_amount_input")
                        )
                    }

                    // Field 4: Bill Description / Note
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "[DESCRIPTION / NOTE]",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            placeholder = {
                                Text(
                                    "e.g. Weekly supplies, invoice #42...",
                                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("due_note_input")
                        )
                    }

                    if (errorText != null) {
                        Text(
                            text = "⚠ $errorText",
                            color = RoseDue,
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Bottom Actions: Cancel and Save Credit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("cancel_due_button")
                        ) {
                            Text("CANCEL", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val amount = amountText.toDoubleOrNull()
                                if (name.isBlank()) {
                                    errorText = "Please enter customer name"
                                } else if (amount == null || amount <= 0) {
                                    errorText = "Please enter a valid amount"
                                } else {
                                    onSave(name, phone, amount, note)
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(48.dp)
                                .testTag("save_due_button")
                        ) {
                            Text("SAVE CREDIT", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordPaymentDialog(
    customerName: String = "",
    currentBalance: Double = 0.0,
    existingCustomers: List<CustomerWithSummary> = emptyList(),
    onDismiss: () -> Unit,
    onSaveCustomerPayment: ((customerId: Long, amount: Double, note: String) -> Unit)? = null,
    onSave: (amount: Double, note: String) -> Unit
) {
    var selectedName by remember { mutableStateOf(customerName) }
    var selectedCustomer by remember {
        mutableStateOf(existingCustomers.find { it.customer.name.equals(customerName.trim(), ignoreCase = true) })
    }
    var amountText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("Cash") }
    var expanded by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val matched = selectedCustomer ?: existingCustomers.find { it.customer.name.equals(selectedName.trim(), ignoreCase = true) }
    val effectiveBalance = matched?.balance ?: currentBalance

    val filteredSuggestions = existingCustomers.filter {
        selectedName.isNotBlank() && it.customer.name.contains(selectedName, ignoreCase = true)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header: Title and Close (X) icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "RECORD PAYMENT",
                                fontSize = 17.sp,
                                fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val subtitleText = if (selectedName.isNotBlank()) {
                                "$selectedName • DUE: ₹${effectiveBalance.toInt()}"
                            } else {
                                "Select customer & enter received amount"
                            }
                            Text(
                                text = subtitleText,
                                fontSize = 11.sp,
                                fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // If customer not preselected, show customer selection
                    if (customerName.isBlank()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "[CUSTOMER] *",
                                fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            ExposedDropdownMenuBox(
                                expanded = expanded && filteredSuggestions.isNotEmpty(),
                                onExpandedChange = { expanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedName,
                                    onValueChange = {
                                        selectedName = it
                                        selectedCustomer = null
                                        expanded = true
                                        errorText = null
                                    },
                                    placeholder = {
                                        Text(
                                            "Select customer...",
                                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                        )
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                        .testTag("payment_customer_input")
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded && filteredSuggestions.isNotEmpty(),
                                    onDismissRequest = { expanded = false }
                                ) {
                                    filteredSuggestions.forEach { item ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column {
                                                        Text(item.customer.name, fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
                                                        if (item.customer.phone.isNotBlank()) {
                                                            Text(
                                                                item.customer.phone,
                                                                fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                                                fontSize = 11.sp,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        text = "₹${item.balance.toInt()} due",
                                                        fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (item.balance > 0) RoseDue else TealSuccess
                                                    )
                                                }
                                            },
                                            onClick = {
                                                selectedName = item.customer.name
                                                selectedCustomer = item
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Field 1: Amount Received (₹) *
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "[AMOUNT RECEIVED (₹)] *",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = amountText,
                            onValueChange = {
                                amountText = it
                                errorText = null
                            },
                            placeholder = {
                                Text(
                                    "e.g. 250",
                                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("payment_amount_input")
                        )
                    }

                    // Field 2: Payment Mode / Reference
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "[PAYMENT MODE / REFERENCE]",
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            placeholder = {
                                Text(
                                    "Cash, UPI, GPay, Bank...",
                                    fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("payment_note_input")
                        )
                    }

                    if (errorText != null) {
                        Text(
                            text = "⚠ $errorText",
                            color = RoseDue,
                            fontFamily = com.example.ui.theme.SpaceMonoFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Bottom Actions: Cancel and Record Payment
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("cancel_payment_button")
                        ) {
                            Text("CANCEL", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val amount = amountText.toDoubleOrNull()
                                if (customerName.isBlank() && matched == null) {
                                    errorText = "Please select a customer"
                                } else if (amount == null || amount <= 0) {
                                    errorText = "Please enter a valid amount"
                                } else {
                                    if (matched != null && onSaveCustomerPayment != null) {
                                        onSaveCustomerPayment(matched.customer.id, amount, note)
                                    } else {
                                        onSave(amount, note)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TealSuccess,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(48.dp)
                                .testTag("save_payment_button")
                        ) {
                            Text("SETTLE PAYMENT", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditCustomerDialog(
    customer: CustomerEntity,
    onDismiss: () -> Unit,
    onSave: (CustomerEntity) -> Unit
) {
    var name by remember { mutableStateOf(customer.name) }
    var phone by remember { mutableStateOf(customer.phone) }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "EDIT CUSTOMER", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorText = null
                    },
                    label = { Text("Customer Name *", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorText != null) {
                    Text(text = "⚠ ${errorText ?: ""}", color = RoseDue, fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 11.5.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorText = "Name cannot be empty"
                    } else {
                        onSave(customer.copy(name = name.trim(), phone = phone.trim()))
                    }
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("SAVE", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text("CANCEL", fontFamily = com.example.ui.theme.SpaceMonoFontFamily)
            }
        }
    )
}

@Composable
fun EditTransactionDialog(
    transaction: TransactionEntity,
    onDismiss: () -> Unit,
    onSave: (TransactionEntity) -> Unit
) {
    var type by remember { mutableStateOf(transaction.type) }
    var amountText by remember { mutableStateOf(transaction.amount.toString()) }
    var note by remember { mutableStateOf(transaction.note) }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "EDIT TRANSACTION", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = type == "credit",
                            onClick = { type = "credit" }
                        )
                        Text("Due Added", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = type == "payment",
                            onClick = { type = "payment" }
                        )
                        Text("Payment", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp)
                    }
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        errorText = null
                    },
                    label = { Text("Amount (₹) *", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Description / Note", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorText != null) {
                    Text(text = "⚠ ${errorText ?: ""}", color = RoseDue, fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 11.5.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        errorText = "Please enter a valid amount"
                    } else {
                        onSave(transaction.copy(type = type, amount = amount, note = note.trim()))
                    }
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("SAVE", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text("CANCEL", fontFamily = com.example.ui.theme.SpaceMonoFontFamily)
            }
        }
    )
}

@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title.uppercase(), fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = { Text(text = message, fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.5.sp) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.buttonColors(containerColor = RoseDue)
            ) {
                Text("DELETE", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text("CANCEL", fontFamily = com.example.ui.theme.SpaceMonoFontFamily)
            }
        }
    )
}

@Composable
fun SortFilterDialog(
    currentSort: String,
    onDismiss: () -> Unit,
    onSelectSort: (String) -> Unit
) {
    val options = listOf(
        "highest_due" to "Highest Due First",
        "lowest_due" to "Lowest Due First",
        "alphabetical" to "Alphabetical (A - Z)",
        "recent" to "Recently Active"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("// SORT ORDER //", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                options.forEach { (key, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSort(key) }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSort == key,
                            onClick = { onSelectSort(key) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(label, fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun EditProfileDialog(
    name: String,
    title: String,
    email: String,
    phone: String,
    onDismiss: () -> Unit,
    onSave: (name: String, title: String, email: String, phone: String) -> Unit
) {
    var newName by remember { mutableStateOf(name) }
    var newTitle by remember { mutableStateOf(title) }
    var newEmail by remember { mutableStateOf(email) }
    var newPhone by remember { mutableStateOf(phone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("EDIT OPERATOR PROFILE", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Full Name *", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Role / Title", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text("Email", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newPhone,
                    onValueChange = { newPhone = it },
                    label = { Text("Phone", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(newName, newTitle, newEmail, newPhone) },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("SAVE PROFILE", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text("CANCEL", fontFamily = com.example.ui.theme.SpaceMonoFontFamily)
            }
        }
    )
}

@Composable
fun InfoDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title.uppercase(), fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = { Text(text = content, fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontSize = 12.sp, lineHeight = 18.sp) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("ACKNOWLEDGE", fontFamily = com.example.ui.theme.SpaceMonoFontFamily, fontWeight = FontWeight.Bold)
            }
        }
    )
}
