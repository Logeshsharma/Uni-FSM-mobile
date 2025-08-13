package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.repository.JobRepository

class CloseJobUseCase(
    private val repository: JobRepository
) {
    suspend operator fun invoke(jobId: String, studentId: String): Result<String> {
        return repository.closeJob(jobId, studentId)
    }
}
