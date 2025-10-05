package com.example.releaseflow.core.domain.model

/**
 * Streaming platforms for music distribution
 */
enum class StreamingPlatform(val displayName: String, val colorHex: String) {
    SPOTIFY("Spotify", "#1DB954"),
    APPLE_MUSIC("Apple Music", "#FA243C"),
    YOUTUBE_MUSIC("YouTube Music", "#FF0000"),
    AMAZON_MUSIC("Amazon Music", "#00A8E1"),
    TIDAL("Tidal", "#000000"),
    DEEZER("Deezer", "#FF0092"),
    SOUNDCLOUD("SoundCloud", "#FF5500"),
    BANDCAMP("Bandcamp", "#629AA9"),
    PANDORA("Pandora", "#3668FF");

    companion object {
        /**
         * Get major platforms (most commonly used)
         */
        fun majorPlatforms(): List<StreamingPlatform> = listOf(
            SPOTIFY,
            APPLE_MUSIC,
            YOUTUBE_MUSIC
        )
    }
}

/**
 * Social media platforms for promotion
 */
enum class SocialPlatform(val displayName: String, val colorHex: String) {
    INSTAGRAM("Instagram", "#E4405F"),
    TIKTOK("TikTok", "#000000"),
    YOUTUBE("YouTube", "#FF0000"),
    TWITTER("Twitter/X", "#1DA1F2"),
    FACEBOOK("Facebook", "#1877F2"),
    SNAPCHAT("Snapchat", "#FFFC00"),
    THREADS("Threads", "#000000"),
    LINKEDIN("LinkedIn", "#0A66C2");

    companion object {
        /**
         * Get primary platforms for music promotion
         */
        fun musicPlatforms(): List<SocialPlatform> = listOf(
            INSTAGRAM,
            TIKTOK,
            YOUTUBE
        )
    }
}
