package com.enovo.contactmanager.screens

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
import com.enovo.contactmanager.DataModel.Contact


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactManagerApp() {
    var contacts by remember { mutableStateOf(listOf<Contact>()) }
    var isPopupOpen by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Filter contacts based on search query
    val filteredContacts = contacts.filter {
        it.name.contains(searchQuery, ignoreCase = true)o
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
            // FAB appears only if contacts are less than MAX_CONTACTS
            if (contacts.size < MAX_CONTACTS) {
                FloatingActionButton(onClick = {
                    selectedContact = null
                    isPopupOpen = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Contact")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Field
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Contacts") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
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
                        onDelete = { contactToDelete ->
                            contacts = contacts.filter { it != contactToDelete }
                        },



                    )
                }
            }
        }
    }

    // Add/Edit Contact Dialog
    if (isPopupOpen) {
        AddEditContactDialog(
            contact = selectedContact,
            onSave = { newContact ->
                contacts = if (selectedContact == null) {
                    contacts + newContact
                } else {
                    contacts.map { if (it == selectedContact) newContact else it }
                }
                isPopupOpen = false
            },
            onCancel = { isPopupOpen = false }
        )
    }
}

// Define a constant for the maximum number of contacts
const val MAX_CONTACTS = 10
