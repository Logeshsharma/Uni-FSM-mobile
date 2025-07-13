package com.uni.fsm.presentation.screens.create_job

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobScreen(viewModel: CreateJobViewModel, userId: String, onBack: () -> Unit) {
    val snackBarHostState = remember { SnackbarHostState() }
    val message = viewModel.message

    LaunchedEffect(message) {
        message?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.message = null
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFD9D2FC),
                    titleContentColor = Color(0xFF000000),
                ),
                title = {
                    Text(
                        "Create Job", maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
                    )
                }, navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = "Back"
                        )
                    }
                })
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { innerPadding ->
        CreateJobForm(viewModel = viewModel, userId = userId, innerPadding = innerPadding)
    }
}


@Composable
fun CreateJobForm(viewModel: CreateJobViewModel, userId: String, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        OutlinedTextField(value = viewModel.title,
            onValueChange = { viewModel.title = it },

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAC9BF8),
                unfocusedBorderColor = Color(0xFF000000),
                focusedLabelColor = Color(0xFFAC9BF8),
            ),
            label = { Text("Title") })
        OutlinedTextField(value = viewModel.description,
            onValueChange = { viewModel.description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAC9BF8),
                unfocusedBorderColor = Color(0xFF000000),
                focusedLabelColor = Color(0xFFAC9BF8),
            ),
            label = { Text("Description") })
        OutlinedTextField(value = viewModel.category,
            onValueChange = { viewModel.category = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAC9BF8),
                unfocusedBorderColor = Color(0xFF000000),
                focusedLabelColor = Color(0xFFAC9BF8),
            ),
            label = { Text("Category") })
        OutlinedTextField(value = viewModel.date,
            onValueChange = { viewModel.date = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAC9BF8),
                unfocusedBorderColor = Color(0xFF000000),
                focusedLabelColor = Color(0xFFAC9BF8),
            ),
            label = { Text("Date (e.g., 12 Jun 1996)") })
        OutlinedTextField(value = viewModel.time,
            onValueChange = { viewModel.time = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAC9BF8),
                unfocusedBorderColor = Color(0xFF000000),
                focusedLabelColor = Color(0xFFAC9BF8),
            ),
            label = { Text("Time (e.g., 12:20)") })
        OutlinedTextField(value = viewModel.address,
            onValueChange = { viewModel.address = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAC9BF8),
                unfocusedBorderColor = Color(0xFF000000),
                focusedLabelColor = Color(0xFFAC9BF8),
            ),
            label = { Text("Address") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.createJob(userId) },
            enabled = !viewModel.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF000000), contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(if (viewModel.isLoading) "Creating..." else "Create Job")
        }
    }
}