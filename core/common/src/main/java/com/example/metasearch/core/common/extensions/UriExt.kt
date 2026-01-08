package com.example.metasearch.core.common.extensions

import android.content.Context
import android.net.Uri
import java.io.File

fun Uri.toFile(context: Context): File {
    val inputStream = context.contentResolver.openInputStream(this)
    val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}
