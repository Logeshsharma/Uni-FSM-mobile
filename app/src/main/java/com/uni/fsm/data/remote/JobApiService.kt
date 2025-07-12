package com.uni.fsm.data.remote

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface JobApiService {
    @POST("/create_job")
    suspend fun createJob(@Body job: CreateJobRequest): Response<CreateJobResponse>
}