package com.example.releaseflow.personal.data.repository

import com.example.releaseflow.personal.data.local.dao.ContactDao
import com.example.releaseflow.personal.data.local.entity.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao
) {
    fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts()

    fun getContactById(id: Long): Flow<Contact?> = contactDao.getContactById(id)

    fun getContactsByType(type: String): Flow<List<Contact>> = 
        contactDao.getContactsByType(type)

    suspend fun insertContact(contact: Contact): Long = contactDao.insertContact(contact)

    suspend fun insertContacts(contacts: List<Contact>): List<Long> = 
        contactDao.insertContacts(contacts)

    suspend fun updateContact(contact: Contact): Int = contactDao.updateContact(contact)

    suspend fun deleteContact(contact: Contact): Int = contactDao.deleteContact(contact)

    suspend fun deleteContactById(id: Long): Int = contactDao.deleteContactById(id)
}
