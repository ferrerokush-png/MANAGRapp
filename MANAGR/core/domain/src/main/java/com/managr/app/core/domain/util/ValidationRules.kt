package com.managr.app.core.domain.util

import java.time.LocalDate

/**
 * Validation rules for domain models
 */
object ValidationRules {
    /**
     * Validate email format
     */
    fun isValidEmail(email: String): Boolean {
        if (email.length < Constants.Validation.MIN_EMAIL_LENGTH ||
            email.length > Constants.Validation.MAX_EMAIL_LENGTH
        ) {
            return false
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    /**
     * Validate URL format
     */
    fun isValidUrl(url: String): Boolean {
        if (url.length < Constants.Validation.MIN_URL_LENGTH ||
            url.length > Constants.Validation.MAX_URL_LENGTH
        ) {
            return false
        }
        val urlRegex = "^(https?://)?(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)$".toRegex()
        return urlRegex.matches(url)
    }

    /**
     * Validate phone number format
     */
    fun isValidPhone(phone: String): Boolean {
        val digitsOnly = phone.replace(Regex("[^0-9]"), "")
        return digitsOnly.length in Constants.Validation.MIN_PHONE_LENGTH..Constants.Validation.MAX_PHONE_LENGTH
    }

    /**
     * Validate project title
     */
    fun isValidProjectTitle(title: String): Boolean {
        return title.length in Constants.Project.MIN_TITLE_LENGTH..Constants.Project.MAX_TITLE_LENGTH
    }

    /**
     * Validate track count for release type
     */
    fun isValidTrackCount(trackCount: Int, releaseType: com.managr.app.core.domain.model.ReleaseType): Boolean {
        return trackCount in releaseType.trackCountRange()
    }

    /**
     * Validate release date (not too far in past or future)
     */
    fun isValidReleaseDate(date: LocalDate): Boolean {
        val today = LocalDate.now()
        val oneYearAgo = today.minusYears(1)
        val twoYearsFromNow = today.plusYears(2)
        return date.isAfter(oneYearAgo) && date.isBefore(twoYearsFromNow)
    }

    /**
     * Validate task title
     */
    fun isValidTaskTitle(title: String): Boolean {
        return title.length in Constants.Task.MIN_TITLE_LENGTH..Constants.Task.MAX_TITLE_LENGTH
    }

    /**
     * Validate file size
     */
    fun isValidFileSize(sizeBytes: Long): Boolean {
        return sizeBytes > 0 && sizeBytes <= Constants.Asset.MAX_FILE_SIZE_BYTES
    }

    /**
     * Validate artwork dimensions
     */
    fun isValidArtworkSize(width: Int, height: Int): Boolean {
        // Must be square
        if (width != height) return false
        
        return width in Constants.Asset.ARTWORK_MIN_SIZE..Constants.Asset.ARTWORK_MAX_SIZE
    }

    /**
     * Validate ISRC code format
     */
    fun isValidISRC(isrc: String): Boolean {
        // Format: CC-ABC-YY-NNNNN (e.g., US-S1Z-99-00001)
        val isrcRegex = "^[A-Z]{2}-[A-Z0-9]{3}-\\d{2}-\\d{5}$".toRegex()
        return isrcRegex.matches(isrc)
    }

    /**
     * Validate UPC code format
     */
    fun isValidUPC(upc: String): Boolean {
        // UPC-A: 12 digits
        val digitsOnly = upc.replace(Regex("[^0-9]"), "")
        return digitsOnly.length == 12
    }

    /**
     * Validate percentage value
     */
    fun isValidPercentage(value: Float): Boolean {
        return value in 0f..100f
    }

    /**
     * Validate playlist follower count
     */
    fun isValidFollowerCount(count: Long): Boolean {
        return count >= 0
    }

    /**
     * Validate date range
     */
    fun isValidDateRange(startDate: LocalDate, endDate: LocalDate): Boolean {
        return !endDate.isBefore(startDate)
    }

    /**
     * Validate revenue amount
     */
    fun isValidRevenueAmount(amount: Double): Boolean {
        return amount >= 0.0
    }

    /**
     * Validate streams count
     */
    fun isValidStreamsCount(streams: Long): Boolean {
        return streams >= 0
    }

    /**
     * Validate engagement rate
     */
    fun isValidEngagementRate(rate: Float): Boolean {
        return rate >= 0f && rate <= 100f
    }

    /**
     * Sanitize text input (remove special characters, trim)
     */
    fun sanitizeText(text: String): String {
        return text.trim().replace(Regex("[<>]"), "")
    }

    /**
     * Validate notes length
     */
    fun isValidNotesLength(notes: String): Boolean {
        return notes.length <= Constants.Validation.MAX_NOTES_LENGTH
    }
}
