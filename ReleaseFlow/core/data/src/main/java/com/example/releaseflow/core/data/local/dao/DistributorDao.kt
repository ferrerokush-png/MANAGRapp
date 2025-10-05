package com.example.releaseflow.core.data.local.dao

import androidx.room.*
import com.example.releaseflow.core.data.local.entity.DistributorEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DistributorDao {
    @Query("SELECT * FROM distributors")
    fun getAllDistributors(): Flow<List<DistributorEntity>>

    @Query("SELECT * FROM distributors WHERE id = :id")
    fun getDistributorById(id: Long): Flow<DistributorEntity?>

    @Query("SELECT * FROM distributors WHERE projectId = :projectId")
    fun getDistributorByProject(projectId: Long): Flow<DistributorEntity?>

    @Query("SELECT * FROM distributors WHERE uploadStatus = :status")
    fun getDistributorsByStatus(status: String): Flow<List<DistributorEntity>>

    @Query("SELECT * FROM distributors WHERE uploadDeadline BETWEEN :today AND :weekFromNow AND uploadStatus NOT IN ('UPLOADED', 'LIVE')")
    fun getDistributorsWithApproachingDeadlines(today: LocalDate, weekFromNow: LocalDate): Flow<List<DistributorEntity>>

    @Query("SELECT * FROM distributors WHERE uploadDeadline < :today AND uploadStatus NOT IN ('UPLOADED', 'LIVE')")
    fun getOverdueDistributors(today: LocalDate): Flow<List<DistributorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistributor(distributor: DistributorEntity): Long

    @Update
    suspend fun updateDistributor(distributor: DistributorEntity)

    @Query("UPDATE distributors SET uploadStatus = :status, updatedAt = :updatedAt WHERE id = :distributorId")
    suspend fun updateUploadStatus(distributorId: Long, status: String, updatedAt: Long)

    @Query("UPDATE distributors SET uploadedDate = :uploadedDate, uploadStatus = 'UPLOADED', updatedAt = :updatedAt WHERE id = :distributorId")
    suspend fun markAsUploaded(distributorId: Long, uploadedDate: LocalDate, updatedAt: Long)

    @Delete
    suspend fun deleteDistributor(distributor: DistributorEntity)
}
