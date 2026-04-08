package com.metasearch.android.data.file.impl.local.repository

import android.content.Context
import androidx.core.net.toUri
import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.file.api.repository.FileRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.io.File

@SingleIn(DataScope::class)
@Inject
class FileRepositoryImpl(
    private val context: Context,
) : FileRepository {
    override suspend fun createTempFileFromUri(uriString: String): Result<File> = runSuspendCatching {
        val uri = uriString.toUri()

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Failed to open InputStream for Uri: $uriString")

        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    }

    override fun deleteFile(file: File): Boolean {
        return try {
            if (file.exists()) {
                file.delete()
            } else {
                true
            }
        } catch (e: Exception) {
            false
        }
    }
}
