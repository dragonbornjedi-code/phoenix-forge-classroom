package com.phoenixforge.profile.domain.auth

/**
 * Parent/steward authentication boundary. Production wiring uses secure storage or biometrics.
 */
interface AuthProvider {
    fun isAuthorized(): Boolean
    suspend fun requestStewardAccess(): AuthResult
    suspend fun enableStewardOnDevice(): AuthResult
    fun clearAuthorization()
}
