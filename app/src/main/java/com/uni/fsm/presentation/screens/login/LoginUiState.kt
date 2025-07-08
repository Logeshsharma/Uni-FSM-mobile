package com.uni.fsm.presentation.screens.login

import com.uni.fsm.domain.model.User

data class LoginUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)