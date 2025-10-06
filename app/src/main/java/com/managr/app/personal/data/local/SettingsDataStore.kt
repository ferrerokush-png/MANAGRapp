package com.managr.app.personal.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val SETTINGS_NAME = "settings"
val Context.settingsDataStore by preferencesDataStore(name = SETTINGS_NAME)

object SettingsKeys {
    val GEMINI_API_KEY = stringPreferencesKey("gemini_api_key")
    val TIMEZONE = stringPreferencesKey("timezone")
    val DISTRIBUTOR = stringPreferencesKey("distributor")
    val CAL_SYNC = booleanPreferencesKey("calendar_sync")

    // Integrations (optional, personal use)
    val SPOTIFY_CLIENT_ID = stringPreferencesKey("spotify_client_id")
    val SPOTIFY_CLIENT_SECRET = stringPreferencesKey("spotify_client_secret")
    val YOUTUBE_API_KEY = stringPreferencesKey("youtube_api_key")
    val APPLE_TEAM_ID = stringPreferencesKey("apple_team_id")
    val APPLE_KEY_ID = stringPreferencesKey("apple_key_id")
    val APPLE_PRIVATE_KEY = stringPreferencesKey("apple_private_key") // consider base64
    val INSTAGRAM_ACCESS_TOKEN = stringPreferencesKey("instagram_access_token")
    val TIKTOK_ACCESS_TOKEN = stringPreferencesKey("tiktok_access_token")
}

class SettingsRepository(private val context: Context) {
    val geminiApiKey: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.GEMINI_API_KEY] ?: "" }
    val timezone: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.TIMEZONE] ?: "Europe/London" }
    val distributor: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.DISTRIBUTOR] ?: "DistroKid" }
    val calendarSync: Flow<Boolean> = context.settingsDataStore.data.map { it[SettingsKeys.CAL_SYNC] ?: true }

    // Integrations
    val spotifyClientId: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.SPOTIFY_CLIENT_ID] ?: "" }
    val spotifyClientSecret: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.SPOTIFY_CLIENT_SECRET] ?: "" }
    val youtubeApiKey: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.YOUTUBE_API_KEY] ?: "" }
    val appleTeamId: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.APPLE_TEAM_ID] ?: "" }
    val appleKeyId: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.APPLE_KEY_ID] ?: "" }
    val applePrivateKey: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.APPLE_PRIVATE_KEY] ?: "" }
    val instagramAccessToken: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.INSTAGRAM_ACCESS_TOKEN] ?: "" }
    val tiktokAccessToken: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.TIKTOK_ACCESS_TOKEN] ?: "" }

    suspend fun setGeminiApiKey(value: String) { context.settingsDataStore.edit { it[SettingsKeys.GEMINI_API_KEY] = value } }
    suspend fun setTimezone(value: String) { context.settingsDataStore.edit { it[SettingsKeys.TIMEZONE] = value } }
    suspend fun setDistributor(value: String) { context.settingsDataStore.edit { it[SettingsKeys.DISTRIBUTOR] = value } }
    suspend fun setCalendarSync(value: Boolean) { context.settingsDataStore.edit { it[SettingsKeys.CAL_SYNC] = value } }

    suspend fun setSpotifyClientId(value: String) { context.settingsDataStore.edit { it[SettingsKeys.SPOTIFY_CLIENT_ID] = value } }
    suspend fun setSpotifyClientSecret(value: String) { context.settingsDataStore.edit { it[SettingsKeys.SPOTIFY_CLIENT_SECRET] = value } }
    suspend fun setYoutubeApiKey(value: String) { context.settingsDataStore.edit { it[SettingsKeys.YOUTUBE_API_KEY] = value } }
    suspend fun setAppleTeamId(value: String) { context.settingsDataStore.edit { it[SettingsKeys.APPLE_TEAM_ID] = value } }
    suspend fun setAppleKeyId(value: String) { context.settingsDataStore.edit { it[SettingsKeys.APPLE_KEY_ID] = value } }
    suspend fun setApplePrivateKey(value: String) { context.settingsDataStore.edit { it[SettingsKeys.APPLE_PRIVATE_KEY] = value } }
    suspend fun setInstagramAccessToken(value: String) { context.settingsDataStore.edit { it[SettingsKeys.INSTAGRAM_ACCESS_TOKEN] = value } }
    suspend fun setTiktokAccessToken(value: String) { context.settingsDataStore.edit { it[SettingsKeys.TIKTOK_ACCESS_TOKEN] = value } }
}
