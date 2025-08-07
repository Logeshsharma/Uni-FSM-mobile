package com.uni.fsm.presentation.screens.job_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uni.fsm.domain.model.Job
import com.uni.fsm.presentation.common.CommonAppBarScaffold

@Composable
fun JobDetailScreen(
    jobId: String,
    onBack: () -> Unit,
    viewModel: JobDetailViewModel,
    navUploadImage: (techId: String) -> Unit = {},
    onCompleteJob: () -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val job = viewModel.jobDetail

    LaunchedEffect(jobId) {
        viewModel.loadJobDetail(jobId)
    }

    CommonAppBarScaffold(
        title = "Job Detail",
        onBack = onBack,
        snackBarHostState = snackBarHostState
    ) { innerPadding ->

        if (viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Black
                )
            }
        } else {
            job?.let {
                JobDetailUI(
                    job = it,
                    innerPadding = innerPadding,
                    navUploadImage = navUploadImage,
                    onCompleteJob = { viewModel.startJob(job.id, job.assigned_to.user_id) },
                    onStartJob = { viewModel.startJob(job.id, job.assigned_to.user_id) }

                )
            } ?: Text(viewModel.errorMessage ?: "Error", color = Color.Red)

        }
    }
}


@Composable
fun JobDetailUI(
    job: Job,
    navUploadImage: (techId: String) -> Unit = {},
    onStartJob: () -> Unit,
    onCompleteJob: () -> Unit,
    innerPadding: PaddingValues,
) {
    val onClickAction = when (job.status) {
        "Assigned" -> onStartJob
        "OnProcess" -> onCompleteJob
        else -> null
    }

    val buttonText = when (job.status) {
        "Assigned" -> "Start Job"
        "OnProcess" -> "Complete Job"
        else -> null
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(innerPadding)
    ) {

        // Job ID
        Text(
            "Job #${job.id}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Title, Address, Assigned Tech & Status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(job.title, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(job.address, fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Assigned to: ${job.assigned_to.username}", fontSize = 14.sp)
            }

            Text(
                text = job.status,
                modifier = Modifier
                    .background(
                        color = when (job.status) {
                            "Assigned" -> Color(0xFF1684DA)
                            "OnProcess" -> Color(0xFF452BCC)
                            "Completed" -> Color(0xFF388E3C)
                            else -> Color.LightGray
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Service Details
        Text("Service Details", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Category: ${job.category}")
            Text("Date: ${job.date}")
            Text("Time: ${job.time}")
            Text("Created by: ${job.createdBy.username}")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Job Description
        Text("Description", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(job.description, fontSize = 14.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { navUploadImage(job.assigned_to.user_id) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF000000),
                contentColor = Color.White
            )
        ) {
            Text("Upload image", fontSize = 16.sp, color = Color.White)
        }

        if (buttonText != null && onClickAction != null) {
            Button(
                onClick = onClickAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000),
                    contentColor = Color.White
                )
            ) {
                Text(buttonText, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}