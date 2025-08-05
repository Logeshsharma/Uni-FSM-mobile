package com.uni.fsm.presentation.screens.job_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.usecase.GetJobDetailsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class JobDetailViewModel(private val useCase: GetJobDetailsUseCase) : ViewModel() {

    var jobDetail by mutableStateOf<Job?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    fun loadJobDetail(jobId: String) {
        viewModelScope.launch {
            isLoading = true
            delay(1000)
            val result  = useCase(jobId)
            result.fold(
                onSuccess = { jobDetail = it },
                onFailure = { errorMessage = it.message }
            )
            isLoading = false
        }
    }
}
