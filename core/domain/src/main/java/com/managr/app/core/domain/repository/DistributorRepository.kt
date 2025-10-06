package com.managr.app.core.domain.repository

import com.managr.app.core.domain.model.Distributor
import com.managr.app.core.domain.model.UploadStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for distributor operations
 */
interface DistributorRepository {
    /**
     * Get all distributors
     */
    fun getAllDistributors(): Flow<List<Distributor>>

    /**
     * Get distributor by ID
     */
    fun getDistributorById(id: Long): Flow<Distributor?>

    /**
     * Get distributor for a project
     */
    fun getDistributorByProject(projectId: Long): Flow<Distributor?>

    /**
     * Get distributors by upload status
     */
    fun getDistributorsByStatus(status: UploadStatus): Flow<List<Distributor>>

    /**
     * Get distributors with approaching deadlines
     */
    fun getDistributorsWithApproachingDeadlines(): Flow<List<Distributor>>

    /**
     * Get overdue distributors
     */
    fun getOverdueDistributors(): Flow<List<Distributor>>

    /**
     * Insert distributor
     */
    suspend fun insertDistributor(distributor: Distributor): Long

    /**
     * Update distributor
     */
    suspend fun updateDistributor(distributor: Distributor)

    /**
     * Update upload status
     */
    suspend fun updateUploadStatus(distributorId: Long, status: UploadStatus)

    /**
     * Mark as uploaded
     */
    suspend fun markAsUploaded(distributorId: Long, uploadedDate: java.time.LocalDate)

    /**
     * Delete distributor
     */
    suspend fun deleteDistributor(distributor: Distributor)
}
