package com.uni.fsm.data.model.response

data class LoginResponse(
    val message: String,
    val status: String,
    val user_id: String?,
    val username: String?,
    val email: String?,
    val role: String?
)
