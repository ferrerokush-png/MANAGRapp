package com.managr.app.personal.ai

import android.content.Context
import com.managr.app.R
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor(
    private val context: Context
) {
    private val apiKey: String by lazy {
        context.getString(R.string.gemini_api_key)
    }

    private fun createModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )
    }

    suspend fun generate(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        if (prompt.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Prompt cannot be empty"))
        }

        try {
            val model = createModel()
            val response = model.generateContent(prompt)
            val text = response.text ?: return@withContext Result.failure(
                IllegalStateException("No response text returned")
            )
            Result.success(text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
