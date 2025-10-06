package com.managr.app.personal.data.repository

import com.managr.app.personal.data.local.entity.Contact
import com.managr.app.personal.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryInterfaceImpl @Inject constructor(
    private val contactRepositoryImpl: ContactRepositoryImpl
) : ContactRepository {
    
    override fun getAllContacts(): Flow<List<Contact>> = 
        contactRepositoryImpl.getAllContacts()
    
    override fun getContactById(id: Long): Flow<Contact?> = 
        contactRepositoryImpl.getContactById(id)
    
    override fun getContactsByType(type: String): Flow<List<Contact>> = 
        contactRepositoryImpl.getContactsByType(type)
    
    override suspend fun insertContact(contact: Contact): Long = 
        contactRepositoryImpl.insertContact(contact)
    
    override suspend fun insertContacts(contacts: List<Contact>): List<Long> = 
        contactRepositoryImpl.insertContacts(contacts)
    
    override suspend fun updateContact(contact: Contact): Int = 
        contactRepositoryImpl.updateContact(contact)
    
    override suspend fun deleteContact(contact: Contact): Int = 
        contactRepositoryImpl.deleteContact(contact)
    
    override suspend fun deleteContactById(id: Long): Int = 
        contactRepositoryImpl.deleteContactById(id)
}
