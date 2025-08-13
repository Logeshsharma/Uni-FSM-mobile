package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.model.User
import com.uni.fsm.domain.repository.LoginRepository

interface LoginUseCase {
    suspend operator fun invoke(username: String, password: String): Result<User>
}

class LoginUseCaseImpl(private val repository: LoginRepository) : LoginUseCase {
    override suspend fun invoke(username: String, password: String): Result<User> {
        return repository.login(username, password)
    }
}
