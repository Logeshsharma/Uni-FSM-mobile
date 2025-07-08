package com.uni.fsm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uni.fsm.presentation.screens.DashboardScreen


@Composable
fun AppNavigationHost(){
    val navController = rememberNavController()
    NavHost(navController= navController, startDestination = "Dashboard"){
        composable("Dashboard") { DashboardScreen() }
    }
}