package com.uni.fsm.presentation.screens.upload_image

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
        if (success) {
            tempUri?.let { uri ->
                viewModel.addBeforeImageUri(uri)
            }
        }
    }

    val afterCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempUri?.let { uri ->
                viewModel.addAfterImageUri(uri)
            }
        }
    }

    val beforePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            tempUri = createImageUri(context)
            beforeCameraLauncher.launch(tempUri!!)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val afterPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            tempUri = createImageUri(context)
            afterCameraLauncher.launch(tempUri!!)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    CommonAppBarScaffold(
        title = "Images", onBack = onBack, snackBarHostState = snackBarHostState
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            Text("Upload Before Images")

            LazyRow(modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp)) {
                items(beforeImageUris.size) { i ->
                    val uri = beforeImageUris[i]
                    AsyncImage(
                        model = uri,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                                beforePermissionLauncher.launch(Manifest.permission.CAMERA)
                            }

                            else -> {
                                tempUri = createImageUri(context)
                                beforeCameraLauncher.launch(tempUri!!)
                            }
                        }
                    },
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF000000), contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Camera, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture Image")
                }

                Button(
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF000000), contentColor = Color.White
                    ),
                    onClick = {
                        val files = beforeImageUris.mapNotNull { uri ->
                            try {
                                uriToFile(context, uri)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                null
                            }
                        }

                        viewModel.uploadImages(jobId = jobId,
                            technicianId = technicianId,
                            type = "before",
                            imageFiles = files,
                            onSuccess = {
                                Toast.makeText(
                                    context, "Uploaded successfully!", Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = { message ->
                                Toast.makeText(
                                    context, "Upload failed: $message", Toast.LENGTH_LONG
                                ).show()
                            })
                    },
                    enabled = beforeImageUris.isNotEmpty() && !uploading
                ) {
                    Icon(Icons.Default.Upload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (uploading) "Uploading..." else "Upload Image")
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(modifier = Modifier.height(2.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text("Upload After Images")

            LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                items(afterImageUris.size) { i ->
                    val uri = afterImageUris[i]
                    AsyncImage(
                        model = uri,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 10.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                                afterPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }

                            else -> {
                                tempUri = createImageUri(context)
                                afterCameraLauncher.launch(tempUri!!)
                            }
                        }
                    },
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF000000), contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Camera, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture Image")
                }

                Button(
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF000000), contentColor = Color.White
                    ),
                    onClick = {
                        val files = afterImageUris.mapNotNull { uri ->
                            try {
                                uriToFile(context, uri)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                null
                            }
                        }

                        viewModel.uploadImages(jobId = jobId,
                            technicianId = technicianId,
                            type = "after",
                            imageFiles = files,
                            onSuccess = {
                                Toast.makeText(
                                    context, "Uploaded successfully!", Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = { message ->
                                Toast.makeText(
                                    context, "Upload failed: $message", Toast.LENGTH_LONG
                                ).show()
                            })
                    },
                    enabled = afterImageUris.isNotEmpty() && !uploading
                ) {
                    Icon(Icons.Default.Upload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (uploading) "Uploading..." else "Upload Image")
                }

            }
        }
    }

}


