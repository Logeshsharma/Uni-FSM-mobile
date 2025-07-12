package com.uni.fsm.presentation.screens.create_job

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CreateJobScreen(viewModel: CreateJobViewModel, userId: String) {

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = viewModel.title, onValueChange = { viewModel.title = it }, label = { Text("Title") })
        OutlinedTextField(value = viewModel.description, onValueChange = { viewModel.description = it }, label = { Text("Description") })
        OutlinedTextField(value = viewModel.category, onValueChange = { viewModel.category = it }, label = { Text("Category") })
        OutlinedTextField(value = viewModel.date, onValueChange = { viewModel.date = it }, label = { Text("Date (e.g., 12 Jun 1996)") })
        OutlinedTextField(value = viewModel.time, onValueChange = { viewModel.time = it }, label = { Text("Time (e.g., 12:20)") })
        OutlinedTextField(value = viewModel.address, onValueChange = { viewModel.address = it }, label = { Text("Address") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.createJob(userId) },
            enabled = !viewModel.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (viewModel.isLoading) "Creating..." else "Create Job")
        }

        viewModel.message?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = if (it.contains("success", true)) Color.Green else Color.Red)
        }
    }
}
