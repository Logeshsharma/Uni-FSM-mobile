package com.uni.fsm.presentation.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    viewModel: JobListViewModel,
    userId: String,
    onLogout: () -> Unit,
    onCreateJob: () -> Unit,
) {
    AppBar(viewModel = viewModel, userId = userId, onLogout, onCreateJob)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    viewModel: JobListViewModel,
    userId: String,
    onLogout: () -> Unit,
    onCreateJob: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFD9D2FC),
                    titleContentColor = Color(0xFF000000),
                ),
                title = {
                    Text(
                        "FSM-StuD",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp)

                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFF000000),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Logout,
                            contentDescription = "Settings",
                            tint = Color(0xFF000000),
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )


        }) { innerPadding ->
        DashboardContent(innerPadding, viewModel = viewModel, userId = userId, onCreateJob)
    }

}

@Composable
fun DashboardContent(
    innerPadding: PaddingValues,
    viewModel: JobListViewModel,
    userId: String,
    onCreateJob: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 20.dp)

    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text("Maintenance!", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Schedule a job to employ the service", style = TextStyle(fontSize = 14.sp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF000000),
                contentColor = Color.White
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
        HorizontalDivider(
            color = Color.Gray,
            thickness = 2.dp
        )
        Spacer(modifier = Modifier.height(10.dp))

        JobListScreen(viewModel = viewModel, userId = userId)

//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(range.count()) { index ->
//                Text(text = "- List item number ${index + 1}")
//            }
//        }

    }
}
