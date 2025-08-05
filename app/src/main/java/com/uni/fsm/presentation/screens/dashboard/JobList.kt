package com.uni.fsm.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uni.fsm.domain.model.Job
import com.uni.fsm.presentation.navigation.SessionViewModel
import com.uni.fsm.presentation.navigation.SessionViewModelFactory

@Composable
fun JobListScreen(viewModel: JobListViewModel, onJobClicked: (Job) -> Unit) {
    val context = LocalContext.current
    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModelFactory(context))
    val user by sessionViewModel.session.collectAsState()
    LaunchedEffect(user) {
        user?.let {
            viewModel.loadJobs(it.user_id, it.role)
        }
    }

    if (viewModel.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Black
            )
        }
    } else {
        LazyColumn {
            items(viewModel.jobs.count()) { index ->
                JobCard(viewModel.jobs[index], onJobClicked)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        viewModel.errorMessage?.let {
            Text(it, color = Color.Red)
        }
    }
}

@Composable
fun JobCard(job: Job, onJobClicked: (Job) -> Unit) {
    Card(
        onClick = { onJobClicked(job) },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardColors(
            containerColor = Color(0xFFD9D2FC),
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.Gray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(job.category, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(job.title, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        job.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Techci", fontSize = 14.sp)
                        Icon(Icons.Default.ArrowForward, contentDescription = "to")
                        Text(job.assigned_to.username, fontSize = 14.sp)
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckBox,
                        contentDescription = "Status",
                        tint = if (job.status == "Completed") Color(0xFF4CAF50) else Color.Gray
                    )
                    Text(job.status, fontSize = 10.sp)
                }
            }

        }
    }
}
