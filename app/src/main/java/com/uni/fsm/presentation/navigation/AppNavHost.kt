package com.uni.fsm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uni.fsm.data.local.SessionManager
import com.uni.fsm.data.remote.client.APIClient
import com.uni.fsm.data.repository.JobRepositoryImpl
import com.uni.fsm.data.repository.LoginRepositoryImpl
import com.uni.fsm.domain.model.User
import com.uni.fsm.domain.usecase.CloseJobUseCase
import com.uni.fsm.domain.usecase.CompleteJobUseCase
import com.uni.fsm.domain.usecase.CompletedJobFilterUseCase
import com.uni.fsm.domain.usecase.CreateJobUseCase
import com.uni.fsm.domain.usecase.GetJobDetailsUseCase
import com.uni.fsm.domain.usecase.GetJobListUseCase
import com.uni.fsm.domain.usecase.LoginUseCaseImpl
import com.uni.fsm.domain.usecase.OnProcessJobFilterUseCase
import com.uni.fsm.domain.usecase.StartJobUseCase
import com.uni.fsm.domain.usecase.UpcomingJobFilterUseCase
import com.uni.fsm.domain.usecase.UploadImagesUseCase
import com.uni.fsm.presentation.screens.create_job.CreateJobScreen
import com.uni.fsm.presentation.screens.create_job.CreateJobViewModel
import com.uni.fsm.presentation.screens.dashboard.DashboardScreen
import com.uni.fsm.presentation.screens.dashboard.JobListViewModel
import com.uni.fsm.presentation.screens.job_detail.JobDetailScreen
import com.uni.fsm.presentation.screens.job_detail.JobDetailViewModel
import com.uni.fsm.presentation.screens.login.LoginScreen
import com.uni.fsm.presentation.screens.login.LoginViewModel
import com.uni.fsm.presentation.screens.upload_image.UploadImagesScreen
import com.uni.fsm.presentation.screens.upload_image.UploadImagesViewModel
import kotlinx.coroutines.launch


@Composable
fun AppNavigationHost() {
    val context = LocalContext.current
    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModelFactory(context))
    val user by sessionViewModel.session.collectAsState()

    val loginRepository = remember { LoginRepositoryImpl(APIClient.createLoginApiService()) }
    val loginUseCase = remember { LoginUseCaseImpl(loginRepository) }

    val loginViewModel = remember { LoginViewModel(loginUseCase) }

    val jobRepository = remember { JobRepositoryImpl(APIClient.createJobApiService()) }
    val createJobUseCase = remember { CreateJobUseCase(jobRepository) }
    val createJobViewModel = remember { CreateJobViewModel(createJobUseCase) }

    val jobListUseCase = remember { GetJobListUseCase(jobRepository) }
    val upcomingJobFilterUseCase = remember { UpcomingJobFilterUseCase() }
    val completedJobFilterUseCase = remember { CompletedJobFilterUseCase() }
    val onProcessJobFilterUseCase = remember { OnProcessJobFilterUseCase() }
    val jobListViewModel = remember {
        JobListViewModel(
            jobListUseCase,
            upcomingJobFilterUseCase,
            completedJobFilterUseCase,
            onProcessJobFilterUseCase
        )
    }

    val getJobDetailsUseCase = remember { GetJobDetailsUseCase(jobRepository) }
    val startJobUseCase = remember { StartJobUseCase(jobRepository) }
    val completeJobUseCase = remember { CompleteJobUseCase(jobRepository) }
    val closeJobUseCase = remember { CloseJobUseCase(jobRepository) }
    val jobDetailViewModel = remember {
        JobDetailViewModel(
            getJobDetailsUseCase, startJobUseCase, completeJobUseCase, closeJobUseCase
        )
    }

    val uploadImagesUseCase = remember { UploadImagesUseCase(jobRepository) }
    val uploadImagesViewModel = remember { UploadImagesViewModel(uploadImagesUseCase) }

    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = getStartDestination(user)) {
        composable("login") {
            LoginScreen(viewModel = loginViewModel) { userId ->
                navController.navigate("Dashboard/$userId") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable(
            "Dashboard/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            DashboardScreen(viewModel = jobListViewModel, onLogout = {
                navController.navigate("login") {
                    popUpTo("Dashboard/{userId}") { inclusive = true }
                }
                coroutineScope.launch {
                    SessionManager.clear(context)
                }
            }, onCreateJob = {
                navController.navigate("create_job/$userId")
            }, onJobClicked = { job ->
                val jobId = job.id
                navController.navigate("job_detail/$jobId")
            }

            )
        }

        composable(
            "create_job/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            CreateJobScreen(viewModel = createJobViewModel, userId = userId) {
                navController.popBackStack()
            }
        }

        composable(
            "job_detail/{jobId}",
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            JobDetailScreen(jobId = jobId, viewModel = jobDetailViewModel, onBack = {
                navController.popBackStack()
            }, navUploadImage = { techId ->
                navController.navigate("upload_images/$jobId/$techId")
            }

            )
        }

        composable(
            "upload_images/{jobId}/{techId}",
            arguments = listOf(navArgument("jobId") { type = NavType.StringType },
                navArgument("techId") { type = NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            val techId = backStackEntry.arguments?.getString("techId") ?: "before"

            UploadImagesScreen(
                jobId = jobId, technicianId = techId, onBack = {
                    navController.popBackStack()
                }, viewModel = uploadImagesViewModel
            )
        }

    }

}

fun getStartDestination(session: User?): String {
    return when {
        session == null -> "login"
        else -> "Dashboard/${session.user_id}"
    }
}
