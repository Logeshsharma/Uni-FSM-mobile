package com.uni.fsm.domain.repository

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse

interface JobRepository {
    suspend fun createJob(jobRequest: CreateJobRequest): Result<CreateJobResponse>
}