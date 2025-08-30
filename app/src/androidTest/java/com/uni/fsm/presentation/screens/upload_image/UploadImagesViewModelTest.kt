package com.uni.fsm.presentation.screens.upload_image

import android.net.Uri
import com.uni.fsm.domain.usecase.UploadImagesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
class UploadImagesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var uploadImagesUseCase: UploadImagesUseCase
    private lateinit var viewModel: UploadImagesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        uploadImagesUseCase = mockk()
        viewModel = UploadImagesViewModel(uploadImagesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addBeforeImageUriupdatesbeforeImageUris() = runTest {
        val uri: Uri = mockk(relaxed = true) // Mocked Uri instead of Uri.parse()
        viewModel.addBeforeImageUri(uri)

        assertEquals(1, viewModel.beforeImageUris.value.size)
        assertEquals(uri, viewModel.beforeImageUris.value.first())
    }

    @Test
    fun addAfterImageUriupdatesafterImageUris() = runTest {
        val uri: Uri = mockk(relaxed = true) // Mocked Uri instead of Uri.parse()
        viewModel.addAfterImageUri(uri)

        assertEquals(1, viewModel.afterImageUris.value.size)
        assertEquals(uri, viewModel.afterImageUris.value.first())
    }

    @Test
    fun uploadImagessuccesscallsonSuccessandupdatesuploadingandafterImageUris() = runTest {
        val jobId = "job1"
        val techId = "tech1"
        val type = "after"
        val files = listOf<File>()
        val uploadedUrls = listOf("url1", "url2")

        coEvery { uploadImagesUseCase(jobId, techId, type, files) } returns Result.success(uploadedUrls)

        var successCalled = false
        var errorCalled = false

        viewModel.uploadImages(
            jobId, techId, type, files,
            onSuccess = { successCalled = true },
            onError = { errorCalled = true }
        )

        advanceUntilIdle()

        assertTrue(successCalled)
        assertFalse(errorCalled)
        assertFalse(viewModel.uploading.value)
        assertEquals(uploadedUrls.size, viewModel.afterImageUris.value.size)
    }

    @Test
    fun uploadImagesfailurecallsonErrorandupdatesuploading() = runTest {
        val jobId = "job1"
        val techId = "tech1"
        val type = "before"
        val files = listOf<File>()

        coEvery { uploadImagesUseCase(jobId, techId, type, files) } returns Result.failure(Exception("Upload failed"))

        var successCalled = false
        var errorMessage: String? = null

        viewModel.uploadImages(
            jobId, techId, type, files,
            onSuccess = { successCalled = true },
            onError = { errorMessage = it }
        )

        advanceUntilIdle()

        assertFalse(successCalled)
        assertEquals("Upload failed", errorMessage)
        assertFalse(viewModel.uploading.value)
    }

    @Test
    fun uploadingstateistrueduringupload() = runTest {
        val jobId = "job1"
        val techId = "tech1"
        val type = "before"
        val files = listOf<File>()

        val uploadDeferred = CompletableDeferred<Result<List<String>>>()
        coEvery { uploadImagesUseCase(jobId, techId, type, files) } returns uploadDeferred.await()

        viewModel.uploadImages(
            jobId, techId, type, files,
            onSuccess = {},
            onError = {}
        )

        // Immediately after launching coroutine
        assertTrue(viewModel.uploading.value)

        // Complete deferred
        uploadDeferred.complete(Result.success(listOf("url1", "url2")))
        advanceUntilIdle()

        assertFalse(viewModel.uploading.value)
    }
}
