package com.uni.fsm.presentation.screens.dashboard

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color

@Composable
fun JobListScreen(viewModel: JobListViewModel, userId: String) {
    LaunchedEffect(Unit) {
        viewModel.loadJobs(userId)
    }

    if (viewModel.isLoading) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(viewModel.jobs.count()) { index ->
                Text("📌 ${viewModel.jobs[index].title} - ${viewModel.jobs[index].status}")
                Text("🕒 ${viewModel.jobs[index].date} ${viewModel.jobs[index].time}")
                Divider()
            }
        }

        viewModel.errorMessage?.let {
            Text("❌ $it", color = Color.Red)
        }
    }
}