package com.example.releaseflow.core.data.local.dao

import androidx.room.*
import com.example.releaseflow.core.data.local.entity.SocialMediaMetricsEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface SocialMediaDao {
    @Query("SELECT * FROM social_media_metrics WHERE projectId = :projectId ORDER BY date DESC")
    fun getSocialMediaMetricsByProject(projectId: Long): Flow<List<SocialMediaMetricsEntity>>

    @Query("SELECT * FROM social_media_metrics WHERE platform = :platform ORDER BY date DESC")
    fun getSocialMediaMetricsByPlatform(platform: String): Flow<List<SocialMediaMetricsEntity>>

    @Query("SELECT * FROM social_media_metrics WHERE platform = :platform AND projectId = :projectId ORDER BY date DESC")
    fun getSocialMediaMetricsByPlatformAndProject(platform: String, projectId: Long): Flow<List<SocialMediaMetricsEntity>>

    @Query("SELECT * FROM social_media_metrics WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getSocialMediaMetricsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<SocialMediaMetricsEntity>>

    @Query("SELECT * FROM social_media_metrics WHERE projectId = :projectId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getSocialMediaMetricsByProjectAndDateRange(projectId: Long, startDate: LocalDate, endDate: LocalDate): Flow<List<SocialMediaMetricsEntity>>

    @Query("SELECT * FROM social_media_metrics ORDER BY date DESC LIMIT 30")
    fun getLatestSocialMediaMetrics(): Flow<List<SocialMediaMetricsEntity>>

    @Query("SELECT * FROM social_media_metrics WHERE projectId = :projectId ORDER BY date DESC LIMIT 30")
    fun getLatestSocialMediaMetricsByProject(projectId: Long): Flow<List<SocialMediaMetricsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialMediaMetrics(metrics: SocialMediaMetricsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialMediaMetrics(metricsList: List<SocialMediaMetricsEntity>): List<Long>

    @Update
    suspend fun updateSocialMediaMetrics(metrics: SocialMediaMetricsEntity)

    @Delete
    suspend fun deleteSocialMediaMetrics(metrics: SocialMediaMetricsEntity)

    @Query("SELECT SUM(followers) FROM social_media_metrics WHERE date = (SELECT MAX(date) FROM social_media_metrics)")
    suspend fun getTotalFollowers(): Long?

    @Query("SELECT SUM(likes + comments + shares) FROM social_media_metrics WHERE projectId = :projectId")
    suspend fun getTotalEngagement(projectId: Long): Long?
}
