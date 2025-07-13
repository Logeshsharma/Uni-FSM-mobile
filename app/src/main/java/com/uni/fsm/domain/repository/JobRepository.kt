package com.uni.fsm.domain.repository

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse
import com.uni.fsm.domain.model.Job

interface JobRepository {
    suspend fun createJob(jobRequest: CreateJobRequest): Result<CreateJobResponse>
    suspend fun getJobs(userId: String, role: String): Result<List<Job>>
}