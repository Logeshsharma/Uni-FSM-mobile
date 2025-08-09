package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.model.Job

class OnProcessJobFilterUseCase {
    operator fun invoke(jobs: List<Job>): Result<List<Job>> {
        val completedJobs = jobs.filter { it.status == "OnProcess" }
        return Result.success(completedJobs)
    }
}


