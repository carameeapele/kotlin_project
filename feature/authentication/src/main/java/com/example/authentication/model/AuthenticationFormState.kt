package com.example.authentication.model

data class AuthFormState(
    val email: String = "",
    val password: String = "",
    val authMode: AuthMode = AuthMode.LOGIN,
    val passwordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)