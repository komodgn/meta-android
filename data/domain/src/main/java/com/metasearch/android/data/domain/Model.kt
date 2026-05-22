package com.metasearch.android.data.domain

import androidx.compose.runtime.Immutable

@Immutable
data class Model(
    val name: String,
    val modelId: String,
    val displayName: String = "",
    val sizeInBytes: Long = 0L,
    var downloadFileName: String = "_",
    var version: String = "_",
    val extraDataFiles: List<ModelDataFile> = listOf(),
    val isZip: Boolean = false,
    val unzipDir: String = "",

    val localFileRelativeDirPathOverride: String = "",
    val localModelFilePathOverride: String = "",
    val imported: Boolean = false,
) {
    var normalizedName: String = ""
    var totalBytes: Long = 0L

    val downloadUrl: String
        get() = "https://huggingface.co/$modelId/resolve/$version/$downloadFileName?download=true"

    init {
        normalizedName = name.replace(Regex("[^a-zA-Z0-9]"), "_")
    }

    fun preProcess() {
        this.totalBytes = this.sizeInBytes + this.extraDataFiles.sumOf { it.sizeInBytes }
    }

    fun getPath(basePath: String, fileName: String = downloadFileName): String {
        val dir = "$basePath/$normalizedName/$version"
        return if (isZip && unzipDir.isNotEmpty()) "$dir/$unzipDir" else "$dir/$fileName"
    }
}

data class ModelDataFile(
    val name: String,
    val url: String,
    val downloadFileName: String,
    val sizeInBytes: Long,
)
