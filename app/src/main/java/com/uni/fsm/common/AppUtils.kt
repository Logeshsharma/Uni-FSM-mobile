package com.uni.fsm.common

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open input stream for URI: $uri")
    val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
    inputStream.use { input ->
        tempFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}


fun createImageUri(context: Context): Uri {
    val filename = "IMG_${System.currentTimeMillis()}.jpg"
    val file = File(context.externalCacheDir, filename)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
