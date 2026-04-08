package com.metasearch.android.domain.file.api.repository

import java.io.File

interface FileRepository {
    suspend fun createTempFileFromUri(uriString: String): Result<File>

    fun deleteFile(file: File): Boolean
}
