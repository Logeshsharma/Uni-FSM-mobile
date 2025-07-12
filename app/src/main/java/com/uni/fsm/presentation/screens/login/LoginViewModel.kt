package com.uni.fsm.presentation.screens.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uni.fsm.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch


class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _uiState = mutableStateOf(LoginUiState())
    val uiState: State<LoginUiState> get() = _uiState

    fun login(username: String, password: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = loginUseCase(username.trim(), password.trim())

            result.fold(
                onSuccess = {
                    Log.i("debug - Logesh", "LoginScreen: ${it.user_id}")
                    _uiState.value = _uiState.value.copy(user = it, isLoading = false)
                    onSuccess(it.user_id)
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        error = it.message ?: "Login failed", isLoading = false
                    )
                }
            )
        }
    }
}

