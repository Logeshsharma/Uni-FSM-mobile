package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.repository.JobRepository

class GetJobDetailsUseCase(private val repository: JobRepository) {
    suspend operator fun invoke(userId: String): Result<Job> {
        return repository.getJobDetails(userId)
    }
}
