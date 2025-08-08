package com.uni.fsm.presentation.screens.upload_image

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uni.fsm.common.createImageUri
import com.uni.fsm.common.uriToFile
import com.uni.fsm.presentation.common.CommonAppBarScaffold

@Composable
fun UploadImagesScreen(
    jobId: String,
    type: String,
    technicianId: String,
    onBack: () -> Unit,
    viewModel: UploadImagesViewModel,
) {
    val context = LocalContext.current
    val beforeImageUris by viewModel.beforeImageUris.collectAsState()
    val afterImageUris by viewModel.afterImageUris.collectAsState()
    val uploading by viewModel.uploading.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val beforeCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) tempUri?.let { uri -> viewModel.addBeforeImageUri(uri) }
    }

    val afterCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) tempUri?.let { uri -> viewModel.addAfterImageUri(uri) }
    }

    val beforePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            tempUri = createImageUri(context)
            beforeCameraLauncher.launch(tempUri!!)
        } else Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    val afterPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            tempUri = createImageUri(context)
            afterCameraLauncher.launch(tempUri!!)
        } else Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    CommonAppBarScaffold(
        title = "Images", onBack = onBack, snackBarHostState = snackBarHostState
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // scrollable content
                .padding(16.dp)
        ) {
            // BEFORE SECTION
            SectionTitle(icon = Icons.Default.PhotoCamera, title = "Before Images")

            LazyRow(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                // Add Button
                item {
                    AddImageCard {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            beforePermissionLauncher.launch(Manifest.permission.CAMERA)
                        } else {
                            tempUri = createImageUri(context)
                            beforeCameraLauncher.launch(tempUri!!)
                        }
                    }
                }
                // Existing images
                items(beforeImageUris.size) { i ->
                    val uri = beforeImageUris[i]
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }

            UploadButton(
                enabled = beforeImageUris.isNotEmpty() && !uploading,
                uploading = uploading,
                onClick = {
                    val files = beforeImageUris.mapNotNull { uri ->
                        runCatching { uriToFile(context, uri) }.getOrNull()
                    }
                    viewModel.uploadImages(
                        jobId = jobId,
                        technicianId = technicianId,
                        type = "before",
                        imageFiles = files,
                        onSuccess = {
                            Toast.makeText(context, "Uploaded successfully!", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onError = { message ->
                            Toast.makeText(context, "Upload failed: $message", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Divider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(24.dp))

            // AFTER SECTION
            SectionTitle(icon = Icons.Default.DoneAll, title = "After Images")

            LazyRow(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                // Add Button
                item {
                    AddImageCard {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            afterPermissionLauncher.launch(Manifest.permission.CAMERA)
                        } else {
                            tempUri = createImageUri(context)
                            afterCameraLauncher.launch(tempUri!!)
                        }
                    }
                }
                // Existing images
                items(afterImageUris.size) { i ->
                    val uri = afterImageUris[i]
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }

            UploadButton(
                enabled = afterImageUris.isNotEmpty() && !uploading,
                uploading = uploading,
                onClick = {
                    val files = afterImageUris.mapNotNull { uri ->
                        runCatching { uriToFile(context, uri) }.getOrNull()
                    }
                    viewModel.uploadImages(
                        jobId = jobId,
                        technicianId = technicianId,
                        type = "after",
                        imageFiles = files,
                        onSuccess = {
                            Toast.makeText(context, "Uploaded successfully!", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onError = { message ->
                            Toast.makeText(context, "Upload failed: $message", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun SectionTitle(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
//        Icon(icon, contentDescription = null, tint = Color.Black)
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun AddImageCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .padding(end = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Image",
                tint = Color.Gray,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun UploadButton(enabled: Boolean, uploading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF000000),
            contentColor = Color.White
        )
    ) {
        Icon(Icons.Default.Upload, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (uploading) "Uploading..." else "Upload Images")
    }
}
