package com.example.releaseflow.core.domain.usecase.crm

import com.example.releaseflow.core.domain.model.Submission
import com.example.releaseflow.core.domain.model.SubmissionStatus
import com.example.releaseflow.core.domain.repository.CRMRepository
import kotlinx.coroutines.flow.Flow

class TrackSubmissionsUseCase(private val crmRepository: CRMRepository) {
    /**
     * Create a new submission
     */
    suspend fun createSubmission(submission: Submission): Result<Long> {
        return try {
            val id = crmRepository.insertSubmission(submission)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Update submission status
     */
    suspend fun updateSubmissionStatus(submissionId: Long, status: SubmissionStatus): Result<Unit> {
        return try {
            crmRepository.updateSubmissionStatus(submissionId, status)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get submissions for a project
     */
    fun getSubmissionsByProject(projectId: Long): Flow<List<Submission>> {
        return crmRepository.getSubmissionsByProject(projectId)
    }
    
    /**
     * Get pending submissions
     */
    fun getPendingSubmissions(): Flow<List<Submission>> {
        return crmRepository.getPendingSubmissions()
    }
}
