package com.uni.fsm.presentation.screens.job_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.usecase.CompleteJobUseCase
import com.uni.fsm.domain.usecase.GetJobDetailsUseCase
import com.uni.fsm.domain.usecase.StartJobUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class JobDetailViewModel(
    private val useCase: GetJobDetailsUseCase,
    private val startJobUseCase: StartJobUseCase,
    private val completeJobUseCase: CompleteJobUseCase,
) : ViewModel() {

    var jobDetail by mutableStateOf<Job?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    var message by mutableStateOf<String?>(null)
        private set

    fun loadJobDetail(jobId: String) {
        viewModelScope.launch {
            isLoading = true
            delay(1000)
            val result = useCase(jobId)
            result.fold(
                onSuccess = { jobDetail = it },
                onFailure = { errorMessage = it.message }
            )
            isLoading = false
        }
    }

    fun startJob(jobId: String, technicianId: String) {
        viewModelScope.launch {
            isLoading = true
            val result = startJobUseCase(jobId, technicianId)
            delay(1000)
            loadJobDetail(jobId)
            result.fold(
                onSuccess = { message = it },
                onFailure = { message = it.message }
            )
            isLoading = false
        }
    }

    fun completeJob(jobId: String, technicianId: String) {
        viewModelScope.launch {
            isLoading = true
            val result = completeJobUseCase(jobId, technicianId)
            delay(1000)
            loadJobDetail(jobId)
            result.fold(
                onSuccess = { message = it },
                onFailure = { message = it.message }
            )
            isLoading = false
        }
    }
}
