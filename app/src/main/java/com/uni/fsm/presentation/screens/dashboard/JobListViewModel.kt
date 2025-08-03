package com.uni.fsm.presentation.screens.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.usecase.GetJobListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JobListViewModel(private val useCase: GetJobListUseCase) : ViewModel() {
    var jobs by mutableStateOf<List<Job>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadJobs(userId: String, role : String) {
        viewModelScope.launch {

            isLoading = true
            val result = useCase(userId, role = role)
            delay(1000)
            result.fold(
                onSuccess = { jobs = it },
                onFailure = { errorMessage = it.message }
            )
            isLoading = false
        }
    }
}