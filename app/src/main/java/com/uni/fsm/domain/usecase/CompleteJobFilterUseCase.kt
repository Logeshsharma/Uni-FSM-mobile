package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.model.Job

class CompletedJobFilterUseCase {
    operator fun invoke(jobs: List<Job>): Result<List<Job>> {
        val completedJobs = jobs.filter { it.status == "Completed" }
        return Result.success(completedJobs)
    }
}