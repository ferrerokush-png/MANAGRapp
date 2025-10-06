package com.managr.app.core.domain.model

/**
 * Type of industry contact in CRM
 */
enum class ContactType(val displayName: String) {
    PLAYLIST_CURATOR("Playlist Curator"),
    BLOGGER("Blogger"),
    JOURNALIST("Journalist"),
    RADIO_HOST("Radio Host"),
    INFLUENCER("Influencer"),
    LABEL_REP("Label Representative"),
    BOOKING_AGENT("Booking Agent"),
    PR_CONTACT("PR Contact"),
    PRODUCER("Producer"),
    COLLABORATOR("Collaborator"),
    OTHER("Other");

    /**
     * Check if this contact type is for playlist submission
     */
    fun isPlaylistContact(): Boolean = this == PLAYLIST_CURATOR

    /**
     * Check if this contact type is for press/media
     */
    fun isMediaContact(): Boolean = this in listOf(BLOGGER, JOURNALIST, RADIO_HOST, INFLUENCER)
}
