package com.enovo.contactmanager.screens

import SQLiteHelper
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.enovo.contactmanager.dataModel.Contact



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactManagerApp(context: Context) {
    val dbHelper = remember { SQLiteHelper(context) }
    var contacts by remember { mutableStateOf(dbHelper.getAllContacts()) }
    var isPopupOpen by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var contactToDelete by remember { mutableStateOf<Contact?>(null) }

    val filteredContacts = contacts.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.email.contains(searchQuery, ignoreCase = true) ||
                it.phone.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Contact Manager", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedContact = null
                isPopupOpen = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Contact")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Contacts") },
                placeholder = { Text("Name, Email, or Phone") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredContacts) { contact ->
                    ContactCard(
                        contact = contact,
                        onEdit = {
                            selectedContact = it
                            isPopupOpen = true
                        },
                        onDelete = { contactToDelete = it }
                    )
                }
            }
        }
    }

    if (isPopupOpen) {
        AddEditContactDialog(
            contact = selectedContact,
            onSave = { newContact ->
                if (selectedContact == null) {
                    dbHelper.insertContact(newContact.name, newContact.email, newContact.phone)
                } else {
                    dbHelper.updateContact(
                        selectedContact!!.id,
                        newContact.name,
                        newContact.email,
                        newContact.phone
                    )
                }
                contacts = dbHelper.getAllContacts()
                isPopupOpen = false
            },
            onCancel = { isPopupOpen = false }
        )
    }

    if (contactToDelete != null) {
        AlertDialog(
            onDismissRequest = { contactToDelete = null },
            title = { Text("Delete Contact?") },
            text = { Text("Are you sure you want to delete ${contactToDelete?.name}?") },
            confirmButton = {
                Button(onClick = {
                    dbHelper.deleteContact(contactToDelete!!.id)
                    contacts = dbHelper.getAllContacts()
                    contactToDelete = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { contactToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}


