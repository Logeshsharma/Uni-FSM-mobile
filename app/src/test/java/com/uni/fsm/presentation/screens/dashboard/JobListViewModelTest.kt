package com.uni.fsm.presentation.screens.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uni.fsm.domain.model.Job
import com.uni.fsm.domain.model.JobUser
import com.uni.fsm.domain.usecase.CompletedJobFilterUseCase
import com.uni.fsm.domain.usecase.GetJobListUseCase
import com.uni.fsm.domain.usecase.OnProcessJobFilterUseCase
import com.uni.fsm.domain.usecase.UpcomingJobFilterUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
class JobListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var getJobListUseCase: GetJobListUseCase
    private lateinit var upcomingJobUseCase: UpcomingJobFilterUseCase
    private lateinit var completedJobUseCase: CompletedJobFilterUseCase
    private lateinit var onProcessJobUseCase: OnProcessJobFilterUseCase
    private lateinit var viewModel: JobListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        getJobListUseCase = mockk()
        upcomingJobUseCase = mockk()
        completedJobUseCase = mockk()
        onProcessJobUseCase = mockk()

        viewModel = JobListViewModel(
            getJobListUseCase, upcomingJobUseCase, completedJobUseCase, onProcessJobUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadJobs success updates job lists correctly`() = runTest {
        val job1 = createJob(id = "1", status = "Assigned")
        val job2 = createJob(id = "2", status = "Completed")
        val job3 = createJob(id = "3", status = "OnProcess")

        val allJobs = listOf(job1, job2, job3)

        coEvery { getJobListUseCase("user123", "student") } returns Result.success(allJobs)

        // Mock filter use cases
        every { upcomingJobUseCase(allJobs) } returns Result.success(listOf(allJobs[0]))
        every { completedJobUseCase(allJobs) } returns Result.success(listOf(allJobs[1]))
        every { onProcessJobUseCase(allJobs) } returns Result.success(listOf(allJobs[2]))

        viewModel.loadJobs("user123", "student")

        // Move virtual time forward for delay(1000)
        advanceTimeBy(1000)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(allJobs, viewModel.jobs)
        assertEquals(listOf(allJobs[0]), viewModel.upcomingJobs)
        assertEquals(listOf(allJobs[1]), viewModel.completedJobs)
        assertEquals(listOf(allJobs[2]), viewModel.onProcessJobs)
        assertEquals(false, viewModel.isLoading)
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `loadJobs failure sets error message`() = runTest {
        val exception = Exception("Network error")
        coEvery { getJobListUseCase("user123", "student") } returns Result.failure(exception)

        viewModel.loadJobs("user123", "student")

        advanceTimeBy(1000)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Network error", viewModel.errorMessage)
        assertEquals(emptyList<Job>(), viewModel.jobs)
        assertEquals(false, viewModel.isLoading)
    }
}


private fun createJob(
    id: String = "1",
    title: String = "Default Title",
    description: String = "Default Description",
    category: String = "Default Category",
    date: String = "2025-08-13",
    time: String = "10:00 AM",
    address: String = "123 Street",
    status: String = "Assigned",
    createdBy: JobUser = JobUser("u1", "creator"),
    assignedTo: JobUser = JobUser("u2", "technician"),
    afterImageUploaded: Boolean = false,
    afterImages: List<String> = emptyList(),
    beforeImageUploaded: Boolean = false,
    beforeImages: List<String> = emptyList(),
    techComplete: Boolean = false,
    studentClosed: Boolean = false,
): Job {
    return Job(
        id,
        title,
        description,
        category,
        date,
        time,
        address,
        status,
        createdBy,
        assignedTo,
        afterImageUploaded,
        afterImages,
        beforeImageUploaded,
        beforeImages,
        techComplete,
        studentClosed
    )
}
