package com.managr.app.core.data.repository

import com.managr.app.core.data.local.dao.CRMDao
import com.managr.app.core.data.local.entity.toEntity
import com.managr.app.core.data.local.entity.toDomain
import com.managr.app.core.domain.model.*
import com.managr.app.core.domain.repository.CRMRepository
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

class CRMRepositoryImpl @Inject constructor(
    private val crmDao: CRMDao
) : CRMRepository {

    override fun getAllContacts(): Flow<List<CRMContact>> =
        crmDao.getAllContacts().map { it.map { e -> e.toDomain() } }

    override fun getContactById(id: Long): Flow<CRMContact?> =
        crmDao.getContactById(id).map { it?.toDomain() }

    override fun getContactsByType(type: ContactType): Flow<List<CRMContact>> =
        crmDao.getContactsByType(type.name).map { it.map { e -> e.toDomain() } }

    override fun getContactsByRelationship(strength: RelationshipStrength): Flow<List<CRMContact>> =
        crmDao.getContactsByRelationship(strength.name).map { it.map { e -> e.toDomain() } }

    override fun getContactsByGenre(genre: String): Flow<List<CRMContact>> =
        getAllContacts().map { contacts -> contacts.filter { it.isRelevantForGenre(genre) } }

    override fun getContactsNeedingFollowUp(): Flow<List<CRMContact>> =
        crmDao.getContactsNeedingFollowUp(LocalDate.now()).map { it.map { e -> e.toDomain() } }

    override fun getInactiveContacts(): Flow<List<CRMContact>> {
        val ninetyDaysAgo = LocalDate.now().minusDays(90)
        return crmDao.getInactiveContacts(ninetyDaysAgo).map { it.map { e -> e.toDomain() } }
    }

    override fun searchContacts(query: String): Flow<List<CRMContact>> =
        crmDao.searchContacts(query).map { it.map { e -> e.toDomain() } }

    override suspend fun insertContact(contact: CRMContact): Long =
        crmDao.insertContact(contact.toEntity())

    override suspend fun updateContact(contact: CRMContact) =
        crmDao.updateContact(contact.toEntity())

    override suspend fun updateLastContactDate(contactId: Long, date: LocalDate) =
        crmDao.updateLastContactDate(contactId, date, System.currentTimeMillis())

    override suspend fun deleteContact(contact: CRMContact) =
        crmDao.deleteContact(contact.toEntity())

    override fun getAllSubmissions(): Flow<List<Submission>> =
        crmDao.getAllSubmissions().map { it.map { e -> e.toDomain() } }

    override fun getSubmissionsByProject(projectId: Long): Flow<List<Submission>> =
        crmDao.getSubmissionsByProject(projectId).map { it.map { e -> e.toDomain() } }

    override fun getSubmissionsByContact(contactId: Long): Flow<List<Submission>> =
        crmDao.getSubmissionsByContact(contactId).map { it.map { e -> e.toDomain() } }

    override fun getSubmissionsByStatus(status: SubmissionStatus): Flow<List<Submission>> =
        crmDao.getSubmissionsByStatus(status.name).map { it.map { e -> e.toDomain() } }

    override fun getPendingSubmissions(): Flow<List<Submission>> =
        crmDao.getPendingSubmissions().map { it.map { e -> e.toDomain() } }

    override suspend fun insertSubmission(submission: Submission): Long =
        crmDao.insertSubmission(submission.toEntity())

    override suspend fun updateSubmission(submission: Submission) =
        crmDao.updateSubmission(submission.toEntity())

    override suspend fun updateSubmissionStatus(submissionId: Long, status: SubmissionStatus) =
        crmDao.updateSubmissionStatus(submissionId, status.name, System.currentTimeMillis())

    override suspend fun deleteSubmission(submission: Submission) =
        crmDao.deleteSubmission(submission.toEntity())

    override suspend fun getContactCountByType(type: ContactType): Int =
        crmDao.getContactCountByType(type.name)

    override suspend fun getTotalContactCount(): Int =
        crmDao.getTotalContactCount()

    override suspend fun deleteAllContacts() =
        crmDao.deleteAllContacts()
}
