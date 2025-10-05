package com.example.releaseflow.core.domain.util

/**
 * Business constants for Release Flow app
 */
object Constants {
    /**
     * Project constants
     */
    object Project {
        const val MIN_TRACK_COUNT = 1
        const val MAX_TRACK_COUNT = 50
        const val MIN_TITLE_LENGTH = 1
        const val MAX_TITLE_LENGTH = 100
        const val MAX_DESCRIPTION_LENGTH = 1000
        const val UPCOMING_DAYS_THRESHOLD = 30
        const val MAX_PROJECTS_PER_USER = 100
    }

    /**
     * Task constants
     */
    object Task {
        const val MIN_TITLE_LENGTH = 1
        const val MAX_TITLE_LENGTH = 200
        const val MAX_DESCRIPTION_LENGTH = 2000
        const val DUE_SOON_DAYS = 3
        const val DEFAULT_REMINDER_DAYS = 1
        const val MAX_DEPENDENCIES = 10
        const val MAX_TASKS_PER_PROJECT = 100
    }

    /**
     * Distributor constants
     */
    object Distributor {
        const val DEFAULT_UPLOAD_DAYS_BEFORE_RELEASE = 21
        const val MINIMUM_UPLOAD_DAYS_BEFORE_RELEASE = 14
        const val DEADLINE_APPROACHING_DAYS = 7
        const val PLAYLIST_PITCH_DAYS_BEFORE_RELEASE = 21
    }

    /**
     * Analytics constants
     */
    object Analytics {
        const val GOOD_ENGAGEMENT_RATE = 3.0f // percentage
        const val EXCELLENT_ENGAGEMENT_RATE = 10.0f // percentage
        const val GOOD_COMPLETION_RATE = 50.0f // percentage
        const val HIGH_SKIP_RATE = 30.0f // percentage
        const val GOOD_STREAMS_PER_LISTENER = 1.5f
        const val VIRAL_POTENTIAL_THRESHOLD = 40.0f
        const val MIN_DATA_POINTS_FOR_PROJECTION = 3
    }

    /**
     * Revenue constants
     */
    object Revenue {
        const val SPOTIFY_AVG_PER_STREAM = 0.004 // USD
        const val APPLE_MUSIC_AVG_PER_STREAM = 0.01 // USD
        const val YOUTUBE_AVG_PER_STREAM = 0.002 // USD
        const val TIDAL_AVG_PER_STREAM = 0.0125 // USD
        const val TYPICAL_PAYOUT_DELAY_DAYS = 60
        const val PAYOUT_OVERDUE_DAYS = 90
    }

    /**
     * CRM constants
     */
    object CRM {
        const val INACTIVE_CONTACT_DAYS = 90
        const val FOLLOW_UP_REMINDER_DAYS = 14
        const val MIN_RESPONSE_RATE_GOOD = 30.0f // percentage
        const val MIN_PLAYLIST_FOLLOWERS_MAJOR = 100000L
        const val MIN_PLAYLIST_FOLLOWERS_MEDIUM = 10000L
        const val MIN_PLAYLIST_FOLLOWERS_SMALL = 1000L
        const val MAX_CONTACTS = 500
    }

    /**
     * Asset constants
     */
    object Asset {
        const val ARTWORK_RECOMMENDED_SIZE = 1500 // pixels (square)
        const val ARTWORK_MIN_SIZE = 1000 // pixels
        const val ARTWORK_MAX_SIZE = 3000 // pixels
        const val MAX_FILE_SIZE_MB = 100
        const val MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024L
        const val MAX_ASSETS_PER_PROJECT = 50
    }

    /**
     * Calendar constants
     */
    object Calendar {
        const val UPCOMING_EVENTS_DAYS = 7
        const val DEFAULT_REMINDER_MINUTES = 1440 // 1 day
        const val MAX_EVENTS_PER_PROJECT = 100
    }

    /**
     * Validation constants
     */
    object Validation {
        const val MIN_EMAIL_LENGTH = 5
        const val MAX_EMAIL_LENGTH = 100
        const val MIN_URL_LENGTH = 10
        const val MAX_URL_LENGTH = 500
        const val MIN_PHONE_LENGTH = 10
        const val MAX_PHONE_LENGTH = 20
        const val MAX_NOTES_LENGTH = 5000
    }

    /**
     * Date format constants
     */
    object DateFormat {
        const val DISPLAY_DATE = "MMM dd, yyyy"
        const val FULL_DATE = "MMMM dd, yyyy"
        const val SHORT_DATE = "MM/dd/yy"
        const val ISO_DATE = "yyyy-MM-dd"
        const val MONTH_YEAR = "MMMM yyyy"
    }

    /**
     * Notification constants
     */
    object Notification {
        val RELEASE_REMINDER_DAYS = listOf(30, 14, 7, 3, 1)
        val TASK_REMINDER_DAYS = listOf(3, 1)
        val UPLOAD_DEADLINE_REMINDER_DAYS = listOf(7, 3, 1)
        const val CHANNEL_ID_RELEASES = "releases"
        const val CHANNEL_ID_TASKS = "tasks"
        const val CHANNEL_ID_INSIGHTS = "insights"
    }

    /**
     * Performance thresholds
     */
    object Performance {
        const val TARGET_FPS = 60
        const val MAX_LIST_ITEMS_BEFORE_PAGINATION = 20
        const val IMAGE_CACHE_SIZE_MB = 50
        const val DATABASE_QUERY_TIMEOUT_MS = 5000L
    }
}
