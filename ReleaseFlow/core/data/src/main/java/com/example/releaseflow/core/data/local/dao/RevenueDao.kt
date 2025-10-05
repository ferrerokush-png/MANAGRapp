package com.example.releaseflow.core.data.local.dao

import androidx.room.*
import com.example.releaseflow.core.data.local.entity.RevenueEntity
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

@Dao
interface RevenueDao {
    @Query("SELECT * FROM revenue ORDER BY period DESC")
    fun getAllRevenue(): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE projectId = :projectId ORDER BY period DESC")
    fun getRevenueByProject(projectId: Long): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE platform = :platform ORDER BY period DESC")
    fun getRevenueByPlatform(platform: String): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE platform = :platform AND projectId = :projectId ORDER BY period DESC")
    fun getRevenueByPlatformAndProject(platform: String, projectId: Long): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE period = :period ORDER BY platform ASC")
    fun getRevenueByPeriod(period: YearMonth): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE period = :period AND projectId = :projectId ORDER BY platform ASC")
    fun getRevenueByPeriodAndProject(period: YearMonth, projectId: Long): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE period BETWEEN :startPeriod AND :endPeriod ORDER BY period DESC")
    fun getRevenueByPeriodRange(startPeriod: YearMonth, endPeriod: YearMonth): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE projectId = :projectId AND period BETWEEN :startPeriod AND :endPeriod ORDER BY period DESC")
    fun getRevenueByProjectAndPeriodRange(projectId: Long, startPeriod: YearMonth, endPeriod: YearMonth): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE isPaid = 0 ORDER BY period DESC")
    fun getAllUnpaidRevenue(): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue WHERE projectId = :projectId AND isPaid = 0 ORDER BY period DESC")
    fun getUnpaidRevenueByProject(projectId: Long): Flow<List<RevenueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevenue(revenue: RevenueEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevenue(revenueList: List<RevenueEntity>): List<Long>

    @Update
    suspend fun updateRevenue(revenue: RevenueEntity)

    @Query("UPDATE revenue SET isPaid = 1, updatedAt = :updatedAt WHERE id = :revenueId")
    suspend fun markAsPaid(revenueId: Long, updatedAt: Long)

    @Delete
    suspend fun deleteRevenue(revenue: RevenueEntity)

    @Query("SELECT SUM(amount) FROM revenue")
    suspend fun getTotalRevenue(): Double?

    @Query("SELECT SUM(amount) FROM revenue WHERE projectId = :projectId")
    suspend fun getTotalRevenueByProject(projectId: Long): Double?

    @Query("SELECT SUM(amount) FROM revenue WHERE isPaid = 0")
    suspend fun getTotalUnpaidRevenue(): Double?

    @Query("SELECT SUM(amount) FROM revenue WHERE projectId = :projectId AND isPaid = 0")
    suspend fun getTotalUnpaidRevenueByProject(projectId: Long): Double?
}
