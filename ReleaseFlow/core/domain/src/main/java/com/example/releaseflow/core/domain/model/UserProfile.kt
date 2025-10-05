package com.example.releaseflow.core.domain.model

data class UserProfile(
    val id: Long = 0,
    val email: String,
    val artistName: String,
    val displayName: String = artistName,
    val bio: String = "",
    val profileImageUri: String? = null,
    val instagramHandle: String? = null,
    val tiktokHandle: String? = null,
    val spotifyArtistId: String? = null,
    val appleMusicArtistId: String? = null,
    val youtubeChannelId: String? = null,
    val isInstagramConnected: Boolean = false,
    val isTikTokConnected: Boolean = false,
    val isSpotifyConnected: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class SocialMediaConnection(
    val id: Long = 0,
    val userId: Long,
    val platform: SocialPlatform,
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresAt: Long,
    val platformUserId: String,
    val platformUsername: String,
    val isActive: Boolean = true,
    val lastSyncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
