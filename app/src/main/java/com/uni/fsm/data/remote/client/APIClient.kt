package com.uni.fsm.data.remote.client

import com.uni.fsm.data.remote.LoginApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APIClient {
    private const val BASE_URL = "https://bus-test-f592.onrender.com"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    fun createJobApiService(): JobApiService = retrofit.create(JobApiService::class.java)

    fun createLoginApiService(): LoginApiService = retrofit.create(LoginApiService::class.java)
}