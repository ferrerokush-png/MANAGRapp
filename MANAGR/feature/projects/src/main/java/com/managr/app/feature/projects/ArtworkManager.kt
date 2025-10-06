package com.managr.app.feature.projects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ArtworkManager @Inject constructor(
    private val context: Context
) {
    
    fun saveArtwork(uri: Uri, projectId: Long): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val artworkDir = File(context.filesDir, "artwork")
            if (!artworkDir.exists()) artworkDir.mkdirs()

            val file = File(artworkDir, "project_${projectId}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    fun validateArtwork(uri: Uri): Result<ArtworkValidation> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return Result.failure(Exception("Cannot open file"))
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            val width = options.outWidth
            val height = options.outHeight
            val fileSize = context.contentResolver.openInputStream(uri)?.available() ?: 0

            val validation = ArtworkValidation(
                isSquare = width == height,
                width = width,
                height = height,
                fileSize = fileSize.toLong(),
                isValidSize = width >= 1000 && height >= 1000,
                isValidFileSize = fileSize <= 10 * 1024 * 1024
            )

            Result.success(validation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun deleteArtwork(projectId: Long) {
        val file = File(context.filesDir, "artwork/project_${projectId}.jpg")
        if (file.exists()) file.delete()
    }
}

data class ArtworkValidation(
    val isSquare: Boolean,
    val width: Int,
    val height: Int,
    val fileSize: Long,
    val isValidSize: Boolean,
    val isValidFileSize: Boolean
) {
    val isValid = isSquare && isValidSize && isValidFileSize
    
    fun errorMessage(): String? = when {
        !isSquare -> "Artwork must be square (same width and height)"
        !isValidSize -> "Artwork must be at least 1000x1000 pixels"
        !isValidFileSize -> "File size must be under 10MB"
        else -> null
    }
}
