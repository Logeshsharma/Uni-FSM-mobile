package com.uni.fsm.data.repository

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse
import com.uni.fsm.data.remote.JobApiService
import com.uni.fsm.domain.repository.JobRepository

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
}