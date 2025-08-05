package com.uni.fsm.data.remote

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse
import com.uni.fsm.data.model.response.JobResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface JobApiService {
    @POST("/mapi/create_job")
    suspend fun createJob(@Body job: CreateJobRequest): Response<CreateJobResponse>

    @GET("/mapi/jobs_list")
    suspend fun getJobs(
        @Query("user_id") userId: String,
        @Query("role") role: String
    ): Response<List<JobResponse>>

    @GET("/mapi/job_detail")
    suspend fun getJobDetail(@Query("job_id") jobId: String): Response<JobResponse>

}