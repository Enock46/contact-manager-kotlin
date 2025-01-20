package com.enovo.contactmanager.DataModel

import android.net.Uri

data class Contact(
    val name: String,
    val phone: String,
    val email: String,
    val photoUri: Uri? = null // Optional image URI
)
