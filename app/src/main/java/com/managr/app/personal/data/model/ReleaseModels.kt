package com.managr.app.personal.data.model

import java.time.LocalDate

enum class ReleaseType { SINGLE, EP, ALBUM }

data class Task(
    val title: String,
    val due: LocalDate,
    val notes: String = "",
    val done: Boolean = false
)

data class ReleaseProject(
    val title: String,
    val type: ReleaseType,
    val releaseDate: LocalDate,
    val tasks: List<Task>
)

object Templates {
    fun generate(type: ReleaseType, title: String, releaseDate: LocalDate): ReleaseProject {
        val base = mutableListOf<Task>()
        // Common tasks
        base += Task("Finalize Master", releaseDate.minusDays(28))
        base += Task("Create Artwork (1500x1500px)", releaseDate.minusDays(24))
        base += Task("Upload to Distributor (DistroKid)", releaseDate.minusDays(21), notes = "So you can pitch to editorial")
        base += Task("Pitch to Playlists (Spotify for Artists)", releaseDate.minusDays(21))
        base += Task("Schedule Social Posts - Teasers", releaseDate.minusDays(20))
        base += Task("Send Press Release / EPK", releaseDate.minusDays(18))
        base += Task("Announce Pre-Save", releaseDate.minusDays(18))
        base += Task("Shoot 3 TikTok/Reels Clips", releaseDate.minusDays(16))
        base += Task("Email Newsletter", releaseDate.minusDays(14))
        base += Task("YouTube Visualizer Upload", releaseDate.minusDays(7))
        base += Task("Release Day Post (All Platforms)", releaseDate)
        base += Task("Thank You / Follow-up Post", releaseDate.plusDays(1))
        base += Task("Pitch to Independent Curators", releaseDate.plusDays(2))

        when (type) {
            ReleaseType.SINGLE -> {
                base += Task("Lyric Video (optional)", releaseDate.plusDays(3))
            }
            ReleaseType.EP -> {
                base += Task("EP Tracklist Finalize", releaseDate.minusDays(30))
                base += Task("EP Artwork Variations", releaseDate.minusDays(25))
                base += Task("Mini videos (12 clips)", releaseDate.minusDays(15))
            }
            ReleaseType.ALBUM -> {
                base += Task("Album Sequence + ISRCs", releaseDate.minusDays(35))
                base += Task("Album Trailer Video", releaseDate.minusDays(20))
                base += Task("Press Outreach Wave 2", releaseDate.plusDays(7))
            }
        }

        val tasks = base.sortedBy { it.due }
        return ReleaseProject(title = title, type = type, releaseDate = releaseDate, tasks = tasks)
    }

    fun distributorUploadBy(releaseDate: LocalDate): LocalDate = releaseDate.minusDays(21)
}

