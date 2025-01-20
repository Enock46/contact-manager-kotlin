package com.enovo.contactmanager.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.enovo.contactmanager.dataModel.Contact

@Composable
fun AddEditContactDialog(
    contact: Contact?,
    onSave: (Contact) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(contact?.name ?: "") }
    var email by remember { mutableStateOf(contact?.email ?: "") }
    var phone by remember { mutableStateOf(contact?.phone ?: "") }

    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text(if (contact == null) "Add Contact" else "Edit Contact") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newContact = Contact(
                    id = (contact?.id ?: 0).toString(), // Use 0 for new contacts, as SQLite will auto-increment
                    name = name,
                    email = email,
                    phone = phone
                )
                onSave(newContact)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = { onCancel() }) {
                Text("Cancel")
            }
        }
    )
}
