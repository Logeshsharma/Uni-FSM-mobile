package com.uni.fsm.presentation.screens.job_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.model.JobUser
import com.uni.fsm.domain.usecase.CloseJobUseCase
import com.uni.fsm.domain.usecase.CompleteJobUseCase
import com.uni.fsm.domain.usecase.GetJobDetailsUseCase
import com.uni.fsm.domain.usecase.StartJobUseCase
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class JobDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getJobDetailsUseCase: GetJobDetailsUseCase
    private lateinit var startJobUseCase: StartJobUseCase
    private lateinit var completeJobUseCase: CompleteJobUseCase
    private lateinit var closeJobUseCase: CloseJobUseCase

    private lateinit var viewModel: JobDetailViewModel

    private val dummyJob = Job(
        id = "1",
        title = "Test Job",
        description = "Test Description",
        category = "Test Category",
        date = "2025-08-13",
        time = "10:00",
        address = "Test Address",
        status = "Assigned",
        createdBy = JobUser("u1", "Creator"),
        assignedTo = JobUser("u2", "Technician"),
        afterImageUploaded = false,
        afterImages = emptyList(),
        beforeImageUploaded = false,
        beforeImages = emptyList(),
        techComplete = false,
        studentClosed = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getJobDetailsUseCase = mockk()
        startJobUseCase = mockk()
        completeJobUseCase = mockk()
        closeJobUseCase = mockk()

        viewModel = JobDetailViewModel(
            getJobDetailsUseCase,
            startJobUseCase,
            completeJobUseCase,
            closeJobUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadJobDetail success updates jobDetail`() = runTest {
        coEvery { getJobDetailsUseCase("1") } returns Result.success(dummyJob)

        viewModel.loadJobDetail("1")
        advanceUntilIdle()

        assertEquals(dummyJob, viewModel.jobDetail)
        assertNull(viewModel.errorMessage)
        assertEquals(false, viewModel.isLoading)
    }

    @Test
    fun `loadJobDetail failure updates errorMessage`() = runTest {
        coEvery { getJobDetailsUseCase("1") } returns Result.failure(Exception("Not found"))

        viewModel.loadJobDetail("1")
        advanceUntilIdle()

        assertEquals("Not found", viewModel.errorMessage)
        assertNull(viewModel.jobDetail)
    }

    @Test
    fun `startJob success sets message`() = runTest {
        coEvery { startJobUseCase("1", "tech1") } returns Result.success("Started")
        coEvery { getJobDetailsUseCase("1") } returns Result.success(dummyJob)

        viewModel.startJob("1", "tech1")
        advanceUntilIdle()

        assertEquals("Started", viewModel.message)
    }

    @Test
    fun `startJob failure sets message`() = runTest {
        coEvery { startJobUseCase("1", "tech1") } returns Result.failure(Exception("Error"))
        coEvery { getJobDetailsUseCase("1") } returns Result.success(dummyJob)

        viewModel.startJob("1", "tech1")
        advanceUntilIdle()

        assertEquals("Error", viewModel.message)
    }

    @Test
    fun `completeJob success sets message`() = runTest {
        coEvery { completeJobUseCase("1", "tech1") } returns Result.success("Completed")
        coEvery { getJobDetailsUseCase("1") } returns Result.success(dummyJob)

        viewModel.completeJob("1", "tech1")
        advanceUntilIdle()

        assertEquals("Completed", viewModel.message)
    }

    @Test
    fun `closeJob success sets message`() = runTest {
        coEvery { closeJobUseCase("1", "student1") } returns Result.success("Closed")
        coEvery { getJobDetailsUseCase("1") } returns Result.success(dummyJob)

        viewModel.closeJob("1", "student1")
        advanceUntilIdle()

        assertEquals("Closed", viewModel.message)
    }
}
