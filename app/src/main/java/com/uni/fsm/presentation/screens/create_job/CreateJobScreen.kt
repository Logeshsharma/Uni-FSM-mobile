package com.uni.fsm.presentation.screens.create_job

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uni.fsm.presentation.common.CommonAppBarScaffold
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    LaunchedEffect(viewModel.navigateBackAfterSuccess) {
        if (viewModel.navigateBackAfterSuccess) {
            delay(500)
            onBack()
            viewModel.reset()
        }
    }
    CommonAppBarScaffold(
        title = "Create Job",
        onBack = onBack,
        snackBarHostState = snackBarHostState,

        ) { innerPadding ->
        CreateJobForm(viewModel = viewModel, userId = userId, innerPadding = innerPadding)
    }
}


@Composable
fun CreateJobForm(viewModel: CreateJobViewModel, userId: String, innerPadding: PaddingValues) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }


    val calendar = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val initialDate = try {
        dateFormat.parse(viewModel.date)
    } catch (e: Exception) {
        null
    }

    if (initialDate != null) {
        calendar.time = initialDate
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                viewModel.date = dateFormat.format(calendar.time)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val initialTime = try {
        timeFormat.parse(viewModel.time)
    } catch (e: Exception) {
        null
    }

    if (initialTime != null) {
        calendar.time = initialTime
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _: TimePicker, hour: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                viewModel.time = timeFormat.format(calendar.time)
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

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
        CategoryDropdown(
            selectedCategory = viewModel.category,
            onCategorySelected = { viewModel.category = it }
        )
        OutlinedTextField(
            value = viewModel.date,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .clickable { showDatePicker = true },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAC9BF8),
                unfocusedBorderColor = Color(0xFF000000),
                focusedLabelColor = Color(0xFFAC9BF8),
                disabledBorderColor = Color(0xFF000000),
                disabledLabelColor = Color(0xFF000000),
                disabledTextColor = Color(0xFF000000),
            ),
            label = { Text("Date (e.g., 12 Jun 1996)") },
            enabled = false,
        )
        OutlinedTextField(
            value = viewModel.time,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .clickable { showTimePicker = true },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFAC9BF8),
                unfocusedBorderColor = Color(0xFF000000),
                focusedLabelColor = Color(0xFFAC9BF8),
                disabledBorderColor = Color(0xFF000000),
                disabledLabelColor = Color(0xFF000000),
                disabledTextColor = Color(0xFF000000),
            ),
            label = { Text("Time (e.g., 12:20)") },
            enabled = false,
        )
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
        OutlinedTextField(value = viewModel.description,
            onValueChange = { viewModel.description = it },
            maxLines = 10,
            minLines = 3,
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