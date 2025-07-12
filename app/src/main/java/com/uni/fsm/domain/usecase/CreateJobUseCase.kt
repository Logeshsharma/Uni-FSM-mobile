package com.uni.fsm.domain.usecase

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse
import com.uni.fsm.domain.repository.JobRepository

class CreateJobUseCase(private val repository: JobRepository) {
    suspend operator fun invoke(jobRequest: CreateJobRequest): Result<CreateJobResponse> {
        return repository.createJob(jobRequest)
    }
}