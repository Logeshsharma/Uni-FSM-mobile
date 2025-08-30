package com.uni.fsm.presentation.screens.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.usecase.CompletedJobFilterUseCase
import com.uni.fsm.domain.usecase.GetJobListUseCase
import com.uni.fsm.domain.usecase.OnProcessJobFilterUseCase
import com.uni.fsm.domain.usecase.UpcomingJobFilterUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JobListViewModel(
    private val getJobListUseCase: GetJobListUseCase,
    private val upcomingJobUseCase: UpcomingJobFilterUseCase,
    private val completedJobUseCase: CompletedJobFilterUseCase,
    private val onProcessJobFilterUseCase: OnProcessJobFilterUseCase,
) : ViewModel() {
    var jobs by mutableStateOf<List<Job>>(emptyList())
    var upcomingJobs by mutableStateOf<List<Job>>(emptyList())
    var completedJobs by mutableStateOf<List<Job>>(emptyList())
    var onProcessJobs by mutableStateOf<List<Job>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadJobs(userId: String, role: String) {
        viewModelScope.launch {
            isLoading = true
            val result = getJobListUseCase(userId, role = role)
            delay(1000)

            result.fold(
                onSuccess = { allJobs ->
                    jobs = allJobs
                    upcomingJobs = upcomingJobUseCase(allJobs).getOrElse { emptyList() }
                    completedJobs = completedJobUseCase(allJobs).getOrElse { emptyList() }
                    onProcessJobs = onProcessJobFilterUseCase(allJobs).getOrElse { emptyList() }
                },
                onFailure = { errorMessage = it.message }
            )
            isLoading = false
        }
    }
}
