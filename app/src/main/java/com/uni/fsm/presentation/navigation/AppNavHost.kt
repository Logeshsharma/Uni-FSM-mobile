package com.uni.fsm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uni.fsm.data.remote.client.APIClient
import com.uni.fsm.data.repository.LoginRepositoryImpl
import com.uni.fsm.domain.usecase.LoginUseCase
import com.uni.fsm.presentation.screens.dashboard.DashboardScreen
import com.uni.fsm.presentation.screens.login.LoginScreen
import com.uni.fsm.presentation.screens.login.LoginViewModel


@Composable
fun AppNavigationHost(){
    val navController = rememberNavController()
    val repository = remember { LoginRepositoryImpl(APIClient.createLoginApiService()) }
    val useCase = remember { LoginUseCase(repository) }
    val viewModel = remember { LoginViewModel(useCase) }
    NavHost(navController= navController, startDestination = "login"){
        composable("Dashboard") { DashboardScreen() }
        composable("login") {
            LoginScreen(viewModel = viewModel) {
                navController.navigate("Dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
}