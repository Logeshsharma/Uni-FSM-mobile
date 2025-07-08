package com.uni.fsm.data.repository

import com.uni.fsm.data.model.request.LoginRequest
import com.uni.fsm.data.remote.LoginApiService
import com.uni.fsm.domain.model.User
import com.uni.fsm.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val api: LoginApiService
) : LoginRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val response = api.loginUser(LoginRequest(username, password))
            if (response.isSuccessful && response.body()?.status == "success") {
                val data = response.body()!!
                Result.success(
                    User(
                        id = data.id.orEmpty(),
                        username = data.username.orEmpty(),
                        email = data.email.orEmpty(),
                        role = data.role.orEmpty()
                    )
                )
            } else {
                Result.failure(Exception(response.body()?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}