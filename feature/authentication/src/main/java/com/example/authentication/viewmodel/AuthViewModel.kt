package com.example.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.model.AuthFormState
import com.example.authentication.model.AuthMode
import com.example.firebase.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthenticationUiState>(AuthenticationUiState.Idle)
    val uiState: StateFlow<AuthenticationUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(AuthFormState())
    val formState: StateFlow<AuthFormState> = _formState.asStateFlow()

    private val _isLogged = MutableStateFlow(repository.getCurrentUser() != null)
    val isLogged: StateFlow<Boolean> = _isLogged.asStateFlow()

    fun updateEmail(email: String) {
        _formState.update {
            it.copy(
                email = email,
                emailError = null
            )
        }
    }

    fun updatePassword(password: String) {
        _formState.update {
            it.copy(
                password = password,
                passwordError = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _formState.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }

    fun toggleAuthMode() {
        _formState.update {
            it.copy(
                authMode = if (it.authMode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN,
                email = "",
                password = "",
                passwordVisible = false,
                emailError = null,
                passwordError = null
            )
        }
        _uiState.value = AuthenticationUiState.Idle
    }

    fun authenticate() {
        val currentForm = _formState.value

        if (currentForm.email.isBlank() || currentForm.password.isBlank()) {
            _formState.update {
                it.copy(
                    emailError = if (currentForm.email.isBlank()) "Email is required" else null,
                    passwordError = if (currentForm.password.isBlank()) "Password is required" else null
                )
            }
            return
        }

        if (currentForm.authMode == AuthMode.REGISTER && currentForm.password.length < 6) {
            _formState.update {
                it.copy(passwordError = "Password must be at least 6 characters long")
            }
            return
        }

        _formState.update {
            it.copy(
                emailError = null,
                passwordError = null
            )
        }

        _uiState.value = AuthenticationUiState.Loading

        viewModelScope.launch {
            val result = if (currentForm.authMode == AuthMode.LOGIN) {
                repository.login(currentForm.email, currentForm.password)
            } else {
                repository.register(currentForm.email, currentForm.password)
            }

            when (result) {
                is com.example.common.util.Resource.Success -> {
                    _uiState.value = AuthenticationUiState.Success(result.data!!)
                    _isLogged.value = true
                }
                is com.example.common.util.Resource.Error -> {
                    handleAuthError(result.message ?: "")
                }
                is com.example.common.util.Resource.Loading -> {}
            }
        }
    }

    private fun handleAuthError(errorMessage: String) {
        val currentMode = _formState.value.authMode

        when {
            currentMode == AuthMode.LOGIN && (
                    errorMessage.contains("no user record", ignoreCase = true) ||
                            errorMessage.contains("invalid-credential", ignoreCase = true) ||
                            errorMessage.contains("invalid-email", ignoreCase = true)
                    ) -> {
                _formState.update {
                    it.copy(passwordError = "Invalid email or password")
                }
                _uiState.value = AuthenticationUiState.Error("Invalid email or password")
            }

            errorMessage.contains("wrong-password", ignoreCase = true) -> {
                _formState.update {
                    it.copy(passwordError = "Incorrect password")
                }
                _uiState.value = AuthenticationUiState.Error("Incorrect password")
            }

            currentMode == AuthMode.REGISTER && errorMessage.contains("already in use", ignoreCase = true) -> {
                _formState.update {
                    it.copy(emailError = "This email is already registered")
                }
                _uiState.value = AuthenticationUiState.Error("This email is already registered")
            }

            errorMessage.contains("badly formatted", ignoreCase = true) ||
                    errorMessage.contains("invalid-email", ignoreCase = true) -> {
                _formState.update {
                    it.copy(emailError = "Invalid email format")
                }
                _uiState.value = AuthenticationUiState.Error("Invalid email format")
            }

            errorMessage.contains("weak-password", ignoreCase = true) -> {
                _formState.update {
                    it.copy(passwordError = "Password must be at least 6 characters")
                }
                _uiState.value = AuthenticationUiState.Error("Password must be at least 6 characters")
            }

            else -> {
                val message = if (currentMode == AuthMode.LOGIN) {
                    "Login failed. Please try again."
                } else {
                    "Registration failed. Please try again."
                }
                _formState.update {
                    it.copy(passwordError = message)
                }
                _uiState.value = AuthenticationUiState.Error(message)
            }
        }
    }

    fun resetAuthState() {
        _uiState.value = AuthenticationUiState.Idle
    }

    fun getCurrentUser() = repository.getCurrentUser()

    suspend fun logout() {
        repository.logout()
        _isLogged.value = false
        _uiState.value = AuthenticationUiState.Idle
        _formState.value = AuthFormState()
    }
}

sealed interface AuthenticationUiState {
    data object Idle : AuthenticationUiState
    data object Loading : AuthenticationUiState
    data class Success(val user: FirebaseUser) : AuthenticationUiState
    data class Error(val message: String) : AuthenticationUiState
}