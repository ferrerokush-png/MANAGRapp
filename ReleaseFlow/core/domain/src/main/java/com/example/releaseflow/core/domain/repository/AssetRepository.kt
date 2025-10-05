package com.example.releaseflow.core.domain.repository

import com.example.releaseflow.core.domain.model.PromotionalAsset
import com.example.releaseflow.core.domain.model.AssetType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for promotional asset operations
 */
interface AssetRepository {
    /**
     * Get all assets
     */
    fun getAllAssets(): Flow<List<PromotionalAsset>>

    /**
     * Get asset by ID
     */
    fun getAssetById(id: Long): Flow<PromotionalAsset?>

    /**
     * Get assets for a project
     */
    fun getAssetsByProject(projectId: Long): Flow<List<PromotionalAsset>>

    /**
     * Get assets by type
     */
    fun getAssetsByType(type: AssetType, projectId: Long? = null): Flow<List<PromotionalAsset>>

    /**
     * Get public assets
     */
    fun getPublicAssets(projectId: Long? = null): Flow<List<PromotionalAsset>>

    /**
     * Search assets
     */
    fun searchAssets(query: String, projectId: Long? = null): Flow<List<PromotionalAsset>>

    /**
     * Insert asset
     */
    suspend fun insertAsset(asset: PromotionalAsset): Long

    /**
     * Update asset
     */
    suspend fun updateAsset(asset: PromotionalAsset)

    /**
     * Delete asset
     */
    suspend fun deleteAsset(asset: PromotionalAsset)

    /**
     * Get asset count for project
     */
    suspend fun getAssetCountByProject(projectId: Long): Int

    /**
     * Get total storage size for project
     */
    suspend fun getTotalStorageSize(projectId: Long): Long
}
