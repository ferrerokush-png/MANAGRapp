package com.managr.app.core.domain.usecase.crm

import com.managr.app.core.domain.model.CRMContact
import com.managr.app.core.domain.repository.CRMRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageCRMContactsUseCase @Inject constructor(private val crmRepository: CRMRepository) {
    /**
     * Create a new contact
     */
    suspend fun createContact(contact: CRMContact): Result<Long> {
        return try {
            contact.validate().getOrElse { return Result.failure(it) }
            val id = crmRepository.insertContact(contact)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Update a contact
     */
    suspend fun updateContact(contact: CRMContact): Result<Unit> {
        return try {
            contact.validate().getOrElse { return Result.failure(it) }
            crmRepository.updateContact(contact)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get all contacts
     */
    fun getAllContacts(): Flow<List<CRMContact>> {
        return crmRepository.getAllContacts()
    }
    
    /**
     * Get contacts needing follow-up
     */
    fun getContactsNeedingFollowUp(): Flow<List<CRMContact>> {
        return crmRepository.getContactsNeedingFollowUp()
    }
}
