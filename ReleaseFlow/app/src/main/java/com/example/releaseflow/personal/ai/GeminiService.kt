package com.example.releaseflow.personal.ai

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiService {
    @Volatile
    private var apiKey: String? = null

    fun configure(key: String) {
        apiKey = key
    }

    private fun modelOrNull(): GenerativeModel? {
        val key = apiKey
        return if (!key.isNullOrBlank()) GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = key
        ) else null
    }

    suspend fun generate(prompt: String): String = withContext(Dispatchers.IO) {
        if (prompt.isBlank()) return@withContext "Please enter a prompt."
        val m = modelOrNull() ?: return@withContext (
            "Gemini API key missing. Go to Settings and add it, or set a resource string named gemini_api_key."
        )
        return@withContext try {
            val response = m.generateContent(prompt)
            response.text ?: "No response text returned."
        } catch (t: Throwable) {
            "AI error: ${t.message ?: "unknown"}"
        }
    }
}
