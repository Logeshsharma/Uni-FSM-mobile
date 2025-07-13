package com.uni.fsm.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uni.fsm.data.remote.client.APIClient
import com.uni.fsm.data.repository.JobRepositoryImpl
import com.uni.fsm.data.repository.LoginRepositoryImpl
import com.uni.fsm.domain.usecase.CreateJobUseCase
import com.uni.fsm.domain.usecase.LoginUseCase
import com.uni.fsm.presentation.screens.create_job.CreateJobScreen
import com.uni.fsm.presentation.screens.create_job.CreateJobViewModel
import com.uni.fsm.presentation.screens.dashboard.DashboardScreen
import com.uni.fsm.presentation.screens.login.LoginScreen
import com.uni.fsm.presentation.screens.login.LoginViewModel


@Composable
fun AppNavigationHost() {
    val navController = rememberNavController()
    val repository = remember { LoginRepositoryImpl(APIClient.createLoginApiService()) }
    val loginUseCase = remember { LoginUseCase(repository) }
    val loginViewModel = remember { LoginViewModel(loginUseCase) }

    val repo = remember { JobRepositoryImpl(APIClient.createJobApiService()) }
    val createJobUseCase = remember { CreateJobUseCase(repo) }
    val createJobViewModel = remember { CreateJobViewModel(createJobUseCase) }
    NavHost(navController = navController, startDestination = "login") {
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
            Log.i("debug - Logesh", "AppNavigationHost: ${userId}")
            DashboardScreen(onLogout = {
                navController.navigate("login") {
                    popUpTo("Dashboard/{userId}") { inclusive = true }
                }
            }, onCreateJob = {
                navController.navigate("create_job/$userId")
            })
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
    }
}