package com.uni.fsm.data.remote

import com.uni.fsm.data.model.request.LoginRequest
import com.uni.fsm.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("/mapi/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
}