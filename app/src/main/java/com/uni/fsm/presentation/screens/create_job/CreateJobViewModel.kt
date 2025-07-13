package com.uni.fsm.presentation.screens.create_job

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.domain.usecase.CreateJobUseCase
import kotlinx.coroutines.launch

class CreateJobViewModel(private val useCase: CreateJobUseCase) : ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var category by mutableStateOf("")
    var date by mutableStateOf("")
    var time by mutableStateOf("")
    var address by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf<String?>(null)
    var navigateBackAfterSuccess by mutableStateOf(false)
        private set

    fun createJob(userId: String) {
        if (!isFormValid()) return
        viewModelScope.launch {
            isLoading = true
            val request = CreateJobRequest(
                user_id = userId,
                title = title,
                description = description,
                job_category = category,
                job_date = date,
                job_time = time,
                address = address
            )
            val result = useCase(request)
            isLoading = false
            message = result.fold(onSuccess = {
                navigateBackAfterSuccess = true
                "Job created successfully!"
            }, onFailure = { it.localizedMessage ?: "Unknown error" })
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            title.isBlank() -> {
                message = "Title is required"
                false
            }

            description.isBlank() -> {
                message = "Description is required"
                false
            }

            category.isBlank() -> {
                message = "Category is required"
                false
            }

            date.isBlank() -> {
                message = "Date is required"
                false
            }

            time.isBlank() -> {
                message = "Time is required"
                false
            }

            address.isBlank() -> {
                message = "Address is required"
                false
            }

            else -> true
        }
    }


    fun reset() {
        navigateBackAfterSuccess = false
        title = ""
        description = ""
        category = ""
        date = ""
        time = ""
        address = ""
        isLoading = false
        message = null
    }
}
