package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.model.User
import com.uni.fsm.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        return repository.login(username, password)
    }
}
