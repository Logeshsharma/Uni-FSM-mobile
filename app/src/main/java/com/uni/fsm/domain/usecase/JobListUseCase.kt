package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.repository.JobRepository

class GetJobListUseCase(private val repository: JobRepository) {
    suspend operator fun invoke(userId: String, role: String): Result<List<Job>> {
        return repository.getJobs(userId, role)
    }
}