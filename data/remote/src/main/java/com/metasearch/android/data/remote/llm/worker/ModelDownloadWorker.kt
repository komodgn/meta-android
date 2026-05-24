package com.metasearch.android.data.remote.llm.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.metasearch.android.core.di.ChildWorkerFactory
import com.metasearch.android.core.di.WorkerKey
import com.metasearch.android.core.di.scope.WorkerScope
import com.metasearch.android.core.worker.api.constants.ModelDownloadKeys
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipInputStream

private const val TAG = "ModelDownloadWorker"
private const val TMP_FILE_EXT = "tmp"

@AssistedInject
class ModelDownloadWorker(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    @AssistedFactory
    @ContributesIntoMap(WorkerScope::class)
    @WorkerKey(ModelDownloadWorker::class)
    interface Factory : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ModelDownloadWorker
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val fileUrl = inputData.getString(ModelDownloadKeys.KEY_MODEL_URL)
        val fileName = inputData.getString(ModelDownloadKeys.KEY_MODEL_DOWNLOAD_FILE_NAME)

        if (fileUrl == null || fileName == null) return@withContext Result.failure()

        try {
            val outputDir = prepareOutputDir()
            val outputFile = File(outputDir, fileName)

            downloadModelFile(fileUrl, outputDir, fileName)

            if (inputData.getBoolean(ModelDownloadKeys.KEY_MODEL_IS_ZIP, false)) {
                val unzippedDir = inputData.getString(ModelDownloadKeys.KEY_MODEL_UNZIPPED_DIR)
                if (unzippedDir != null) unzipModelFile(outputFile, File(outputDir, unzippedDir))
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Download failed", e)
            Result.failure()
        }
    }

    private fun prepareOutputDir(): File {
        val modelDir = inputData.getString(ModelDownloadKeys.KEY_MODEL_DOWNLOAD_MODEL_DIR) ?: "models"
        val version = inputData.getString(ModelDownloadKeys.KEY_MODEL_COMMIT_HASH) ?: "v1"
        return applicationContext.getExternalFilesDir(null)?.let {
            File(it, listOf(modelDir, version).joinToString(File.separator)).apply {
                if (!exists()) mkdirs()
            }
        } ?: throw IOException("External files dir not found")
    }

    private suspend fun downloadModelFile(fileUrl: String, outputDir: File, fileName: String) {
        val tmpFile = File(outputDir, "$fileName.$TMP_FILE_EXT")
        val connection = createConnection(fileUrl, tmpFile.length())
        val totalBytes = inputData.getLong(ModelDownloadKeys.KEY_MODEL_TOTAL_BYTES, 1L)
        val modelName = inputData.getString(ModelDownloadKeys.KEY_MODEL_NAME) ?: "Unknown"

        connection.inputStream.use { input ->
            FileOutputStream(tmpFile, true).use { output ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalBytesRead = tmpFile.length()

                var lastProgressUpdate = 0L
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                    totalBytesRead += bytesRead

                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastProgressUpdate > 1000) {
                        val progress = totalBytesRead.toFloat() / totalBytes.toFloat()

                        setProgress(
                            workDataOf(
                                "KEY_PROGRESS" to progress,
                                ModelDownloadKeys.KEY_MODEL_NAME to modelName,
                            ),
                        )
                        lastProgressUpdate = currentTime
                    }
                }
            }
        }

        if (!tmpFile.renameTo(File(outputDir, fileName))) {
            throw IOException("Failed to rename temporary file")
        }
    }

    private fun createConnection(fileUrl: String, range: Long): HttpURLConnection {
        return (URL(fileUrl).openConnection() as HttpURLConnection).apply {
            setRequestProperty("User-Agent", "Android-Model-Downloader")
            inputData.getString("KEY_MODEL_DOWNLOAD_ACCESS_TOKEN")?.let {
                setRequestProperty("Authorization", "Bearer $it")
            }
            if (range > 0) setRequestProperty("Range", "bytes=$range-")
            connectTimeout = 30_000
            readTimeout = 600_000
        }
    }

    private fun unzipModelFile(zipFile: File, destDir: File) {
        if (!destDir.exists()) destDir.mkdirs()
        val canonicalDestDir = destDir.canonicalFile
        ZipInputStream(BufferedInputStream(FileInputStream(zipFile))).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val target = File(destDir, entry.name).canonicalFile
                if (!target.path.startsWith(canonicalDestDir.path + File.separator)) {
                    throw IOException("Zip entry escapes destination: ${entry.name}")
                }
                if (entry.isDirectory) {
                    target.mkdirs()
                } else {
                    target.parentFile?.mkdirs()
                    FileOutputStream(target).use { zis.copyTo(it) }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
        zipFile.delete()
    }
}
