package com.managr.app.core.domain.model

/**
 * Type of music release
 */
enum class ReleaseType {
    SINGLE,
    EP,
    ALBUM;

    /**
     * Get display name for UI
     */
    fun displayName(): String = when (this) {
        SINGLE -> "Single"
        EP -> "EP"
        ALBUM -> "Album"
    }

    /**
     * Get recommended track count range
     */
    fun trackCountRange(): IntRange = when (this) {
        SINGLE -> 1..3
        EP -> 4..7
        ALBUM -> 8..20
    }

    /**
     * Get typical task count for this release type
     */
    fun typicalTaskCount(): Int = when (this) {
        SINGLE -> 15
        EP -> 20
        ALBUM -> 25
    }
}
