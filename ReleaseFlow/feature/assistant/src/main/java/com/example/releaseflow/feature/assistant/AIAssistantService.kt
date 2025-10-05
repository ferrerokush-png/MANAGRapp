package com.example.releaseflow.feature.assistant

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class AIAssistantService @Inject constructor() {

    private val model = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "" // Will be set from BuildConfig
    )

    suspend fun getReleaseStrategyAdvice(
        projectTitle: String,
        releaseType: String,
        genre: String?,
        daysUntilRelease: Long
    ): String {
        val prompt = buildString {
            append("You are an experienced music industry manager. ")
            append("An artist is releasing a $releaseType")
            if (genre != null) append(" in the $genre genre")
            append(" titled '$projectTitle' in $daysUntilRelease days. ")
            append("Provide 3 specific, actionable recommendations for maximizing the release success. ")
            append("Focus on social media, playlisting, and promotion timing. Keep it concise and practical.")
        }

        return try {
            val response = model.generateContent(prompt)
            response.text ?: "Unable to generate advice at this time."
        } catch (e: Exception) {
            "AI assistant temporarily unavailable. Focus on uploading to your distributor 21 days before release and pitching to playlists."
        }
    }

    suspend fun analyzeAnalytics(
        totalStreams: Long,
        monthlyListeners: Long,
        topPlatform: String
    ): String {
        val prompt = buildString {
            append("As a music industry analyst, review these metrics: ")
            append("$totalStreams total streams, $monthlyListeners monthly listeners, ")
            append("with $topPlatform as the top platform. ")
            append("Provide 2-3 specific actionable insights to grow these numbers. Be concise.")
        }

        return try {
            val response = model.generateContent(prompt)
            response.text ?: "Keep promoting your music consistently across all platforms."
        } catch (e: Exception) {
            "Focus on creating engaging content for your top platform ($topPlatform) and cross-promote to other channels."
        }
    }

    suspend fun generateContentIdeas(
        projectTitle: String,
        releaseType: String,
        daysUntilRelease: Long
    ): List<String> {
        val prompt = buildString {
            append("Generate 5 specific social media content ideas for promoting ")
            append("a $releaseType titled '$projectTitle' releasing in $daysUntilRelease days. ")
            append("Include ideas for TikTok, Instagram Reels, and YouTube. Be specific and actionable.")
        }

        return try {
            val response = model.generateContent(prompt)
            response.text?.split("\n")?.filter { it.isNotBlank() }?.take(5) ?: defaultContentIdeas()
        } catch (e: Exception) {
            defaultContentIdeas()
        }
    }

    private fun defaultContentIdeas() = listOf(
        "Create a 15-second teaser with the hook",
        "Behind-the-scenes of the recording process",
        "Lyric breakdown video explaining the meaning",
        "Fan reaction compilation",
        "Acoustic version or remix preview"
    )
}
