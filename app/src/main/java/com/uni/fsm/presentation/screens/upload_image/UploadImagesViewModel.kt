package com.uni.fsm.presentation.screens.upload_image

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uni.fsm.domain.usecase.UploadImagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class UploadImagesViewModel(
    private val uploadImagesUseCase: UploadImagesUseCase,
) : ViewModel() {

    val beforeImageUris = MutableStateFlow<List<Uri>>(emptyList())

    fun addBeforeImageUri(uri: Uri) {
        beforeImageUris.value += uri
    }

    val afterImageUris = MutableStateFlow<List<Uri>>(emptyList())

    fun addAfterImageUri(uri: Uri) {
        afterImageUris.value += uri
    }

    private val _uploading = MutableStateFlow(false)
    val uploading: StateFlow<Boolean> = _uploading.asStateFlow()


    fun uploadImages(
        jobId: String,
        technicianId: String,
        type: String,
        imageFiles: List<File>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            _uploading.value = true

            val result = uploadImagesUseCase(jobId, technicianId, type, imageFiles)

            _uploading.value = false

            if (result.isSuccess) {
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

}
