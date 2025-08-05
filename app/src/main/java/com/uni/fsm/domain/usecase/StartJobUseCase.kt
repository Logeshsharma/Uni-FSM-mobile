package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.repository.JobRepository

class StartJobUseCase(private val repository: JobRepository) {
    suspend operator fun invoke(jobId: String, technicianId: String): Result<String> {
        return repository.startJob(jobId, technicianId)
    }
}
