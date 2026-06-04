package com.phoenixforge.profile.domain.auth

sealed class AuthResult {
    data object Granted : AuthResult()
    data object Denied : AuthResult()
    data object NotConfigured : AuthResult()
}
