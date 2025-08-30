package com.uni.fsm.presentation.screens.create_job

import com.uni.fsm.data.model.request.CreateJobRequest
import com.uni.fsm.data.model.response.CreateJobResponse
import com.uni.fsm.domain.usecase.CreateJobUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateJobViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CreateJobViewModel
    private lateinit var useCase: CreateJobUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        useCase = mockk()
        viewModel = CreateJobViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun formValidation_failsWhenFieldsAreEmpty() {
        viewModel.createJob("user1")

        assertEquals("Title is required", viewModel.message)
        assertFalse(viewModel.navigateBackAfterSuccess)
    }

    @Test
    fun createJob_success_setsNavigateBackAndMessage() = runTest {
        // Fill form
        viewModel.title = "Fix Sink"
        viewModel.description = "Leaking sink"
        viewModel.category = "Bathroom"
        viewModel.date = "20 Aug 2025"
        viewModel.time = "10:30"
        viewModel.address = "123 Street"

        val mockResponse = CreateJobResponse(job_id = "123", message = "Created")

        coEvery { useCase(any<CreateJobRequest>()) } returns Result.success(mockResponse)

        viewModel.createJob("user1")
        advanceUntilIdle()

        assertTrue(viewModel.navigateBackAfterSuccess)
        assertEquals("Job created successfully!", viewModel.message)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun createJob_failure_setsErrorMessage() = runTest {
        // Fill form
        viewModel.title = "Fix Sink"
        viewModel.description = "Leaking sink"
        viewModel.category = "Bathroom"
        viewModel.date = "20 Aug 2025"
        viewModel.time = "10:30"
        viewModel.address = "123 Street"

        coEvery { useCase(any<CreateJobRequest>()) } returns Result.failure(Exception("Server error"))

        viewModel.createJob("user1")
        advanceUntilIdle()

        assertFalse(viewModel.navigateBackAfterSuccess)
        assertEquals("Server error", viewModel.message)
        assertFalse(viewModel.isLoading)
    }
}
