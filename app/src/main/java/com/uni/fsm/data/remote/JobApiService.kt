package com.uni.fsm.data.remote

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.request.StartJobRequest
import com.uni.fsm.data.model.response.CommonResponse
import com.uni.fsm.data.model.response.CreateJobResponse
import com.uni.fsm.data.model.response.JobResponse
import com.uni.fsm.data.model.response.UploadImagesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST("/mapi/start_job")
    suspend fun startJob(@Body request: StartJobRequest): Response<CommonResponse>

    @Multipart
    @POST("mapi/upload_image")
    suspend fun uploadImages(
        @Part("job_id") jobId: RequestBody,
        @Part("type") type: RequestBody,
        @Part("technician_id") technicianId: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<UploadImagesResponse>

}