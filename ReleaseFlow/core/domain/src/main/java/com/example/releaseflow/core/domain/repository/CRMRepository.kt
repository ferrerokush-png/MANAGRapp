package com.example.releaseflow.core.domain.repository

import com.example.releaseflow.core.domain.model.CRMContact
import com.example.releaseflow.core.domain.model.ContactType
import com.example.releaseflow.core.domain.model.RelationshipStrength
import com.example.releaseflow.core.domain.model.Submission
import com.example.releaseflow.core.domain.model.SubmissionStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for CRM operations
 */
interface CRMRepository {
    /**
     * Get all contacts
     */
    fun getAllContacts(): Flow<List<CRMContact>>

    /**
     * Get contact by ID
     */
    fun getContactById(id: Long): Flow<CRMContact?>

    /**
     * Get contacts by type
     */
    fun getContactsByType(type: ContactType): Flow<List<CRMContact>>

    /**
     * Get contacts by relationship strength
     */
    fun getContactsByRelationship(strength: RelationshipStrength): Flow<List<CRMContact>>

    /**
     * Get contacts for a genre
     */
    fun getContactsByGenre(genre: String): Flow<List<CRMContact>>

    /**
     * Get contacts needing follow-up
     */
    fun getContactsNeedingFollowUp(): Flow<List<CRMContact>>

    /**
     * Get inactive contacts
     */
    fun getInactiveContacts(): Flow<List<CRMContact>>

    /**
     * Search contacts
     */
    fun searchContacts(query: String): Flow<List<CRMContact>>

    /**
     * Insert contact
     */
    suspend fun insertContact(contact: CRMContact): Long

    /**
     * Update contact
     */
    suspend fun updateContact(contact: CRMContact)

    /**
     * Update last contact date
     */
    suspend fun updateLastContactDate(contactId: Long, date: java.time.LocalDate)

    /**
     * Delete contact
     */
    suspend fun deleteContact(contact: CRMContact)

    /**
     * Get all submissions
     */
    fun getAllSubmissions(): Flow<List<Submission>>

    /**
     * Get submissions for a project
     */
    fun getSubmissionsByProject(projectId: Long): Flow<List<Submission>>

    /**
     * Get submissions for a contact
     */
    fun getSubmissionsByContact(contactId: Long): Flow<List<Submission>>

    /**
     * Get submissions by status
     */
    fun getSubmissionsByStatus(status: SubmissionStatus): Flow<List<Submission>>

    /**
     * Get pending submissions
     */
    fun getPendingSubmissions(): Flow<List<Submission>>

    /**
     * Insert submission
     */
    suspend fun insertSubmission(submission: Submission): Long

    /**
     * Update submission
     */
    suspend fun updateSubmission(submission: Submission)

    /**
     * Update submission status
     */
    suspend fun updateSubmissionStatus(submissionId: Long, status: SubmissionStatus)

    /**
     * Delete submission
     */
    suspend fun deleteSubmission(submission: Submission)

    /**
     * Get contact count by type
     */
    suspend fun getContactCountByType(type: ContactType): Int

    /**
     * Get total contact count
     */
    suspend fun getTotalContactCount(): Int
}
