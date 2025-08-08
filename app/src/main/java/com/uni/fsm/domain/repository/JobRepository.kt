package com.uni.fsm.domain.repository

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse
import com.uni.fsm.domain.model.Job
import java.io.File

interface JobRepository {
    suspend fun createJob(jobRequest: CreateJobRequest): Result<CreateJobResponse>
    suspend fun getJobs(userId: String, role: String): Result<List<Job>>
    suspend fun getJobDetails(jobId: String): Result<Job>
    suspend fun startJob(jobId: String, technicianId: String): Result<String>
    suspend fun uploadImages(
        jobId: String,
        technicianId: String,
        type: String,
        imageFiles: List<File>,
    ): Result<List<String>>

    suspend fun completeJob(jobId: String, technicianId: String): Result<String>

}