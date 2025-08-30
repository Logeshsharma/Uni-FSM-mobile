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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
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
fun JobListScreen(viewModel: JobListViewModel, onJobClicked: (Job) -> Unit, isStudent: Boolean) {
    val context = LocalContext.current
    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModelFactory(context))
    val user by sessionViewModel.session.collectAsState()

    LaunchedEffect(user) {
        user?.let {
            viewModel.loadJobs(it.user_id, it.role)
        }
    }

    if (viewModel.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Black)
        }
    } else {
        if (isStudent) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (viewModel.jobs.isNotEmpty()) {
                    items(viewModel.jobs.size) { i ->
                        JobCard(viewModel.jobs[i], onJobClicked)
                    }
                } else {
                    item {
                        Text(
                            "No Jobs Found",
                            fontSize = 16.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (viewModel.onProcessJobs.isNotEmpty()) {
                    item {
                        Text(
                            "Current Job",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    items(viewModel.onProcessJobs.size) { i ->
                        JobCard(viewModel.onProcessJobs[i], onJobClicked)
                    }
                }

                if (viewModel.upcomingJobs.isNotEmpty()) {
                    item {
                        Text(
                            "Upcoming Jobs",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    items(viewModel.upcomingJobs.size) { i ->
                        JobCard(viewModel.upcomingJobs[i], onJobClicked)
                    }
                }

                if (viewModel.completedJobs.isNotEmpty()) {
                    item {
                        Text(
                            "Completed Jobs",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    items(viewModel.completedJobs.size) { i ->
                        JobCard(viewModel.completedJobs[i], onJobClicked)
                    }
                }
                if (viewModel.jobs.isEmpty()) {
                    item {
                        Text(
                            "No Jobs Found",
                            fontSize = 16.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                }
            }
        }



        viewModel.errorMessage?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun JobCard(job: Job, onJobClicked: (Job) -> Unit) {
    Card(
        onClick = { onJobClicked(job) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D2FC)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Job Icon",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(job.category, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(job.title, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        job.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person, contentDescription = "Technician", tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        job.assignedTo.username, style = MaterialTheme.typography.bodyMedium
                    )
                }

                StatusChip(job.status)
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val textColor = when (status) {
        "Assigned" -> Color(0xFF1280D5)
        "OnProcess" -> Color(0xFF4126CE)
        "Completed" -> Color(0xFF08960B)
        "Closed" -> Color(0xFF008080)
        "Pending" -> Color(0xFF3A2802)
        else -> Color.LightGray
    }

    val backgroundColor = textColor.copy(alpha = 0.15f)

    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium
        )
    }
}

