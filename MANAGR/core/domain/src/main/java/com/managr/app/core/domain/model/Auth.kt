package com.managr.app.core.domain.model

data class AuthUser(
    val uid: String,
    val email: String,
    val displayName: String?,
    val photoUrl: String?,
    val isEmailVerified: Boolean = false,
    val accountType: AccountType = AccountType.ARTIST,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AccountType {
    ARTIST,
    LABEL,
    MANAGER
}

data class ArtistProfile(
    val id: String = "",
    val userId: String,
    val artistName: String,
    val stageName: String = artistName,
    val bio: String = "",
    val genre: List<String> = emptyList(),
    val profileImageUrl: String? = null,
    val coverImageUrl: String? = null,
    val location: String? = null,
    val website: String? = null,
    val spotifyArtistId: String? = null,
    val appleMusicArtistId: String? = null,
    val instagramUsername: String? = null,
    val tiktokUsername: String? = null,
    val youtubeChannelId: String? = null,
    val isVerified: Boolean = false,
    val isPrimary: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class LabelProfile(
    val id: String = "",
    val userId: String,
    val labelName: String,
    val description: String = "",
    val logoUrl: String? = null,
    val website: String? = null,
    val artists: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class SocialMediaAuth(
    val id: String = "",
    val userId: String,
    val platform: SocialPlatform,
    val platformUserId: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresAt: Long,
    val scopes: List<String> = emptyList(),
    val isActive: Boolean = true,
    val canPost: Boolean = false,
    val canReadAnalytics: Boolean = false,
    val lastSyncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
