package com.example.releaseflow.core.data.local.dao

import androidx.room.*
import com.example.releaseflow.core.data.local.entity.CRMContactEntity
import com.example.releaseflow.core.data.local.entity.SubmissionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CRMDao {
    // Contact operations
    @Query("SELECT * FROM crm_contacts WHERE isActive = 1 ORDER BY name ASC")
    fun getAllContacts(): Flow<List<CRMContactEntity>>

    @Query("SELECT * FROM crm_contacts WHERE id = :id")
    fun getContactById(id: Long): Flow<CRMContactEntity?>

    @Query("SELECT * FROM crm_contacts WHERE type = :type AND isActive = 1 ORDER BY name ASC")
    fun getContactsByType(type: String): Flow<List<CRMContactEntity>>

    @Query("SELECT * FROM crm_contacts WHERE relationshipStrength = :strength AND isActive = 1 ORDER BY name ASC")
    fun getContactsByRelationship(strength: String): Flow<List<CRMContactEntity>>

    @Query("SELECT * FROM crm_contacts WHERE nextFollowUpDate <= :today AND isActive = 1 ORDER BY nextFollowUpDate ASC")
    fun getContactsNeedingFollowUp(today: LocalDate): Flow<List<CRMContactEntity>>

    @Query("SELECT * FROM crm_contacts WHERE lastContactDate < :ninetyDaysAgo OR lastContactDate IS NULL AND isActive = 1 ORDER BY lastContactDate ASC")
    fun getInactiveContacts(ninetyDaysAgo: LocalDate): Flow<List<CRMContactEntity>>

    @Query("SELECT * FROM crm_contacts WHERE name LIKE '%' || :query || '%' OR company LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchContacts(query: String): Flow<List<CRMContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: CRMContactEntity): Long

    @Update
    suspend fun updateContact(contact: CRMContactEntity)

    @Query("UPDATE crm_contacts SET lastContactDate = :date, updatedAt = :updatedAt WHERE id = :contactId")
    suspend fun updateLastContactDate(contactId: Long, date: LocalDate, updatedAt: Long)

    @Delete
    suspend fun deleteContact(contact: CRMContactEntity)

    @Query("SELECT COUNT(*) FROM crm_contacts WHERE type = :type AND isActive = 1")
    suspend fun getContactCountByType(type: String): Int

    @Query("SELECT COUNT(*) FROM crm_contacts WHERE isActive = 1")
    suspend fun getTotalContactCount(): Int

    // Submission operations
    @Query("SELECT * FROM submissions ORDER BY submissionDate DESC")
    fun getAllSubmissions(): Flow<List<SubmissionEntity>>

    @Query("SELECT * FROM submissions WHERE projectId = :projectId ORDER BY submissionDate DESC")
    fun getSubmissionsByProject(projectId: Long): Flow<List<SubmissionEntity>>

    @Query("SELECT * FROM submissions WHERE contactId = :contactId ORDER BY submissionDate DESC")
    fun getSubmissionsByContact(contactId: Long): Flow<List<SubmissionEntity>>

    @Query("SELECT * FROM submissions WHERE status = :status ORDER BY submissionDate DESC")
    fun getSubmissionsByStatus(status: String): Flow<List<SubmissionEntity>>

    @Query("SELECT * FROM submissions WHERE status = 'PENDING' ORDER BY submissionDate DESC")
    fun getPendingSubmissions(): Flow<List<SubmissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: SubmissionEntity): Long

    @Update
    suspend fun updateSubmission(submission: SubmissionEntity)

    @Query("UPDATE submissions SET status = :status, updatedAt = :updatedAt WHERE id = :submissionId")
    suspend fun updateSubmissionStatus(submissionId: Long, status: String, updatedAt: Long)

    @Delete
    suspend fun deleteSubmission(submission: SubmissionEntity)
}
