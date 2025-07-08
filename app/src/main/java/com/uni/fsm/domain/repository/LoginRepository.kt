package com.uni.fsm.domain.repository

import com.uni.fsm.domain.model.User

interface LoginRepository {
    suspend fun login(username: String, password: String): Result<User>
}