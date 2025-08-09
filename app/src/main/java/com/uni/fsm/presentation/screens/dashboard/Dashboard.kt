package com.uni.fsm.presentation.screens.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uni.fsm.domain.model.Job
import com.uni.fsm.presentation.navigation.SessionViewModel
import com.uni.fsm.presentation.navigation.SessionViewModelFactory


@Composable
fun DashboardScreen(
    viewModel: JobListViewModel,
    onLogout: () -> Unit,
    onCreateJob: () -> Unit,
    onJobClicked: (Job) -> Unit,
) {
    AppBar(viewModel = viewModel, onLogout, onCreateJob, onJobClicked)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    viewModel: JobListViewModel,
    onLogout: () -> Unit,
    onCreateJob: () -> Unit,
    onJobClicked: (Job) -> Unit,
) {
    val context = LocalContext.current
    val sessionViewModel: SessionViewModel = viewModel(factory = SessionViewModelFactory(context))
    val user by sessionViewModel.session.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFD9D2FC),
            titleContentColor = Color(0xFF000000),
            scrolledContainerColor = Color(0xFFD9D2FC),
        ), title = {

            Text(
                if (user?.role == "Student") "FSM-StuD" else "FSM-Tech",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp)

            )
        }, navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile",
                    tint = Color(0xFF000000),
                )
            }
        }, actions = {
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = "Settings",
                    tint = Color(0xFF000000),
                )
            }
        }, scrollBehavior = scrollBehavior
        )


    }) { innerPadding ->
        DashboardContent(
            innerPadding,
            viewModel = viewModel,
            onCreateJob,
            onJobClicked = onJobClicked,
            isStudent = user?.role == "Student"
        )
    }

}

@Composable
fun DashboardContent(
    innerPadding: PaddingValues,
    viewModel: JobListViewModel,
    onCreateJob: () -> Unit,
    onJobClicked: (Job) -> Unit,
    isStudent: Boolean,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 20.dp)

    ) {
        Spacer(modifier = Modifier.height(25.dp))
        if (isStudent) {
            Text("Maintenance!", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Schedule a job to employ the service", style = TextStyle(fontSize = 14.sp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000), contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = onCreateJob,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text("Create a Job")
            }
            Spacer(modifier = Modifier.height(10.dp))
        } else {
            JobSummarySection(
                totalJobCount = viewModel.jobs.size, completedCount = viewModel.completedJobs.size
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        HorizontalDivider(
            color = Color.Gray, thickness = 2.dp
        )
        Spacer(modifier = Modifier.height(14.dp))
        if (isStudent) Text(
            text = "Job History",
            modifier = Modifier.padding(bottom = 20.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
        )
        JobListScreen(viewModel = viewModel, onJobClicked = onJobClicked, isStudent = isStudent)
    }
}


@Composable
fun JobSummarySection(totalJobCount: Int, completedCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryItem(
            label = "Total Jobs",
            count = totalJobCount,
            backgroundColor = Color(0xFFE3F2FD),
            countColor = Color(0xFF1684DA)
        )

        SummaryItem(
            label = "Completed",
            count = completedCount,
            backgroundColor = Color(0xFFE8F5E9),
            countColor = Color(0xFF388E3C)
        )
    }
}

@Composable
fun SummaryItem(label: String, count: Int, backgroundColor: Color, countColor: Color) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .width(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = count.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = countColor
            )
            Text(
                text = label, fontSize = 14.sp, color = Color.Black
            )
        }
    }
}
