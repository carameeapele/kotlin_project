package com.example.pictionis.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firebase.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(authRepository.getCurrentUser() != null)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    fun checkAuthState() {
        _isAuthenticated.value = authRepository.getCurrentUser() != null
    }

    suspend fun onLogout() {
        authRepository.logout()
        _isAuthenticated.value = false
    }
}