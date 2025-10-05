package com.example.releaseflow.core.data.local.dao

import androidx.room.*
import com.example.releaseflow.core.data.local.entity.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM promotional_assets")
    fun getAllAssets(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM promotional_assets WHERE id = :id")
    fun getAssetById(id: Long): Flow<AssetEntity?>

    @Query("SELECT * FROM promotional_assets WHERE projectId = :projectId")
    fun getAssetsByProject(projectId: Long): Flow<List<AssetEntity>>

    @Query("SELECT * FROM promotional_assets WHERE type = :type")
    fun getAssetsByType(type: String): Flow<List<AssetEntity>>

    @Query("SELECT * FROM promotional_assets WHERE projectId = :projectId AND type = :type")
    fun getAssetsByProjectAndType(projectId: Long, type: String): Flow<List<AssetEntity>>

    @Query("SELECT * FROM promotional_assets WHERE isPublic = 1")
    fun getPublicAssets(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM promotional_assets WHERE projectId = :projectId AND isPublic = 1")
    fun getPublicAssetsByProject(projectId: Long): Flow<List<AssetEntity>>

    @Query("SELECT * FROM promotional_assets WHERE name LIKE '%' || :query || '%'")
    fun searchAssets(query: String): Flow<List<AssetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: AssetEntity): Long

    @Update
    suspend fun updateAsset(asset: AssetEntity)

    @Delete
    suspend fun deleteAsset(asset: AssetEntity)

    @Query("SELECT COUNT(*) FROM promotional_assets WHERE projectId = :projectId")
    suspend fun getAssetCountByProject(projectId: Long): Int

    @Query("SELECT SUM(fileSize) FROM promotional_assets WHERE projectId = :projectId")
    suspend fun getTotalStorageSize(projectId: Long): Long?
}
