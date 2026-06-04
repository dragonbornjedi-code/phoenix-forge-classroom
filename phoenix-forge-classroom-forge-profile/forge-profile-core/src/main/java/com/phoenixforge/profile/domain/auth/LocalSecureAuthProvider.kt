package com.phoenixforge.profile.domain.auth

import android.content.SharedPreferences

/**
 * Device-local steward gate backed by private SharedPreferences.
 * Production wiring can swap to EncryptedSharedPreferences or biometrics.
 */
class LocalSecureAuthProvider(
    private val prefs: SharedPreferences
) : AuthProvider {

    override fun isAuthorized(): Boolean = prefs.getBoolean(KEY_STEWARD_AUTH, false)

    override suspend fun requestStewardAccess(): AuthResult =
        if (prefs.getBoolean(KEY_STEWARD_AUTH, false)) {
            AuthResult.Granted
        } else {
            AuthResult.NotConfigured
        }

    override suspend fun enableStewardOnDevice(): AuthResult {
        prefs.edit().putBoolean(KEY_STEWARD_AUTH, true).apply()
        return AuthResult.Granted
    }

    override fun clearAuthorization() {
        prefs.edit().putBoolean(KEY_STEWARD_AUTH, false).apply()
    }

    private companion object {
        const val KEY_STEWARD_AUTH = "steward_auth_state"
    }
}
