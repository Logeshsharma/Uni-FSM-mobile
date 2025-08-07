package com.uni.fsm.domain.usecase

import com.uni.fsm.domain.repository.JobRepository
import java.io.File

class UploadImagesUseCase(
    private val repository: JobRepository
) {
    suspend operator fun invoke(
        jobId: String,
        technicianId: String,
        type: String,
        imageFiles: List<File>
    ): Result<List<String>> {
        return repository.uploadImages(jobId, technicianId, type, imageFiles)
    }
}