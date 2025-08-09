package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.model.Job

class UpcomingJobFilterUseCase {
    operator fun invoke(jobs: List<Job>): Result<List<Job>> {
        val completedJobs = jobs.filter { it.status == "Assigned" }
        return Result.success(completedJobs)
    }
}


