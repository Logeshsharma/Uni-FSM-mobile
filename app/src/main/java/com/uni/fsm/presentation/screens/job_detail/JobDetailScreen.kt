package com.uni.fsm.presentation.screens.job_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.uni.fsm.domain.model.Job
import com.uni.fsm.presentation.common.CommonAppBarScaffold
import com.uni.fsm.presentation.navigation.SessionViewModel
import com.uni.fsm.presentation.navigation.SessionViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun JobDetailScreen(
    jobId: String,
    onBack: () -> Unit,
    viewModel: JobDetailViewModel,
    navUploadImage: (techId: String) -> Unit = {},

    ) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val job = viewModel.jobDetail
    val coroutineScope = rememberCoroutineScope()
    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModelFactory(context))
    val user by sessionViewModel.session.collectAsState()

    LaunchedEffect(jobId) {
        viewModel.loadJobDetail(jobId)
    }



    CommonAppBarScaffold(
        title = "Job Detail", onBack = onBack, snackBarHostState = snackBarHostState
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
                JobDetailUI(job = it,
                    innerPadding = innerPadding,
                    navUploadImage = navUploadImage,
                    onCompleteJob = {
                        if (job.beforeImageUploaded && job.afterImageUploaded) {
                            viewModel.completeJob(job.id, job.assignedTo.userId)

                        } else {
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(
                                    "Please upload images!", duration = SnackbarDuration.Short
                                )
                            }
                        }

                    },

                    onStartJob = { viewModel.startJob(job.id, job.assignedTo.userId) },
                    isStudent = user?.role == "Student",
                    onCloseJob = { viewModel.closeJob(job.id, job.createdBy.userId) })
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
    onCloseJob: () -> Unit,
    innerPadding: PaddingValues,
    isStudent: Boolean,
) {
    val onClickAction = when (job.status) {
        "Assigned" -> onStartJob
        "OnProcess" -> onCompleteJob
        "Completed" -> onCloseJob
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
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Job #${job.id}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(job.title, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(job.address, fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Assigned to: ${job.assignedTo.username}", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                StatusChip(job.status)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionCard(title = "Service Details") {
            Text("Category: ${job.category}")
            Text("Date: ${job.date}")
            Text("Time: ${job.time}")
            Text("Created by: ${job.createdBy.username}")
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionCard(title = "Description") {
            Text(job.description, fontSize = 14.sp, color = Color.Black)
        }



        JobImagesSection(job.beforeImages, job.afterImages)

        if (!isStudent) {


            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (buttonText != null && onClickAction != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .clickable { navUploadImage(job.assignedTo.userId) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Upload Image",
                                modifier = Modifier.size(32.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Upload Image", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onClickAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000))
                    ) {
                        Text(buttonText, fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        } else {
            if (job.status == "Completed" && job.techComplete) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (onClickAction != null) {
                        Button(
                            onClick = onClickAction,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000))
                        ) {
                            Text("Close Job", fontSize = 16.sp, color = Color.White)
                        }
                    }

                }
            }
        }

    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D2FC)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun JobImagesSection(
    beforeImages: List<String>,
    afterImages: List<String>,
) {
    Column(modifier = Modifier.padding(16.dp)) {

        if (beforeImages.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Before Images", modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow {
                items(beforeImages.size) { index ->
                    AsyncImage(
                        model = beforeImages[index],
                        contentDescription = "Before Image $index",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (afterImages.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "After Images", modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow {
                items(afterImages.size) { index ->
                    AsyncImage(
                        model = afterImages[index],
                        contentDescription = "After Image $index",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
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
        else -> Color.LightGray
    }

    val backgroundColor = textColor.copy(alpha = 0.15f)

    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
