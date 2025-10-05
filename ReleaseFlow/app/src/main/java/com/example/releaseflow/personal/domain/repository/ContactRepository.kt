package com.example.releaseflow.personal.domain.repository

import com.example.releaseflow.personal.data.local.entity.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<Contact>>
    fun getContactById(id: Long): Flow<Contact?>
    fun getContactsByType(type: String): Flow<List<Contact>>
    suspend fun insertContact(contact: Contact): Long
    suspend fun insertContacts(contacts: List<Contact>): List<Long>
    suspend fun updateContact(contact: Contact): Int
    suspend fun deleteContact(contact: Contact): Int
    suspend fun deleteContactById(id: Long): Int
}
