package com.example.releaseflow.core.domain.model

/**
 * Music distribution services
 */
enum class DistributorType(
    val displayName: String,
    val website: String,
    val minUploadDays: Int
) {
    DISTROKID("DistroKid", "https://distrokid.com", 21),
    CD_BABY("CD Baby", "https://cdbaby.com", 21),
    TUNECORE("TuneCore", "https://tunecore.com", 21),
    AMUSE("Amuse", "https://amuse.io", 14),
    DITTO("Ditto Music", "https://dittomusic.com", 21),
    AWAL("AWAL", "https://awal.com", 28),
    UNITED_MASTERS("UnitedMasters", "https://unitedmasters.com", 21),
    STEM("Stem", "https://stem.is", 21),
    LANDR("LANDR", "https://landr.com", 14),
    OTHER("Other", "", 21);

    companion object {
        /**
         * Get popular distributors
         */
        fun popularDistributors(): List<DistributorType> = listOf(
            DISTROKID,
            CD_BABY,
            TUNECORE
        )
    }
}
