package com.uni.fsm.data.repository

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.request.StartJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse
import com.uni.fsm.data.model.response.toDomain
import com.uni.fsm.data.remote.JobApiService
import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.repository.JobRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class JobRepositoryImpl(private val apiService: JobApiService) : JobRepository {
    override suspend fun createJob(jobRequest: CreateJobRequest): Result<CreateJobResponse> {
        return try {
            val response = apiService.createJob(jobRequest)
            val data = response.body()
            if (response.isSuccessful && data != null) {
                Result.success(
                    CreateJobResponse(
                        job_id = data.job_id.orEmpty(),
                        message = data.message.orEmpty(),
                    )
                )
            } else Result.failure(Exception("Error: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getJobs(userId: String, role: String): Result<List<Job>> {
        return try {
            val response = apiService.getJobs(userId, role)
            if (response.isSuccessful) {
                val jobs = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(jobs)
            } else {
                Result.failure(Exception("Failed to fetch jobs"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getJobDetails(jobId: String): Result<Job> {
        return try {
            val response = apiService.getJobDetail(jobId)
            if (response.isSuccessful) {
                val jobResponse = response.body() ?: throw Exception("Empty response")

                Result.success(jobResponse.toDomain())
            } else Result.failure(Exception("Failed to fetch job detail"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun startJob(jobId: String, technicianId: String): Result<String> {
        return try {
            val response = apiService.startJob(StartJobRequest(jobId, technicianId))
            if (response.isSuccessful) {
                val message = response.body()?.message ?: "Success"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to start job"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadImages(
        jobId: String,
        technicianId: String,
        type: String,
        imageFiles: List<File>
    ): Result<List<String>> {
        return try {
            val parts = imageFiles.map { file ->
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            }

            val response = apiService.uploadImages(
                jobId = jobId.toRequestBody("text/plain".toMediaTypeOrNull()),
                type = type.toRequestBody("text/plain".toMediaTypeOrNull()),
                technicianId = technicianId.toRequestBody("text/plain".toMediaTypeOrNull()),
                images = parts
            )

            if (response.isSuccessful) {
                Result.success(response.body()?.urls ?: emptyList())
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}