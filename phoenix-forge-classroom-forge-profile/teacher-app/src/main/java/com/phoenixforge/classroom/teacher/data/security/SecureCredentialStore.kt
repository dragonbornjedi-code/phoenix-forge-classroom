package com.phoenixforge.classroom.teacher.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureCredentialStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun hasApiKey(): Boolean = !getApiKey().isNullOrBlank()

    fun getApiKey(): String? = prefs.getString(KEY_API, null)?.takeIf { it.isNotBlank() }

    fun setApiKey(value: String) {
        prefs.edit().putString(KEY_API, value.trim()).apply()
    }

    fun clearApiKey() {
        prefs.edit().remove(KEY_API).apply()
    }

    fun getProviderUrl(): String =
        prefs.getString(KEY_PROVIDER_URL, DEFAULT_PROVIDER_URL) ?: DEFAULT_PROVIDER_URL

    fun setProviderUrl(value: String) {
        prefs.edit().putString(KEY_PROVIDER_URL, value.trim()).apply()
    }

    fun getModelId(): String =
        prefs.getString(KEY_MODEL, DEFAULT_MODEL) ?: DEFAULT_MODEL

    fun setModelId(value: String) {
        prefs.edit().putString(KEY_MODEL, value.trim()).apply()
    }

    companion object {
        private const val PREFS_NAME = "sage_secure_credentials"
        private const val KEY_API = "api_key"
        private const val KEY_PROVIDER_URL = "provider_url"
        private const val KEY_MODEL = "model_id"
        const val DEFAULT_PROVIDER_URL = "https://openrouter.ai/api/v1/chat/completions"
        const val DEFAULT_MODEL = "google/gemma-2-9b-it:free"
    }
}
