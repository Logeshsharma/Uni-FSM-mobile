package com.uni.fsm.data.model.response

import com.uni.fsm.domain.model.User

data class LoginResponse(
    val message: String,
    val status: String,
    val user_id: String?,
    val username: String?,
    val email: String?,
    val role: String?,
)


fun LoginResponse.toDomain(): User {
    return User(
        user_id = user_id ?: "", username = username ?: "", email = email ?: "", role = role ?: ""
    )
}