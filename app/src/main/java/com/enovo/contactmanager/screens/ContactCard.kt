package com.enovo.contactmanager.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import com.enovo.contactmanager.DataModel.*



@Composable
fun ContactCard(contact: Contact, onEdit: (Contact) -> Unit, onDelete: (Contact) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp,end=16.dp,top=10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                modifier = Modifier.fillMaxWidth().padding(16.dp)


    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Column {
                    Text(text = "Name: ${contact.name}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Phone: ${contact.phone}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Email: ${contact.email}", style = MaterialTheme.typography.bodyLarge)
                }

            }
            Row {
                IconButton(onClick = { onEdit(contact) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Contact")
                }
                IconButton(onClick = { onDelete(contact) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Contact")
                }
            }
        }
    }
}
