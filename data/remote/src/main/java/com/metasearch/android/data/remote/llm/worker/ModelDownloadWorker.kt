package com.metasearch.android.data.remote.llm.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.metasearch.android.core.di.ChildWorkerFactory
import com.metasearch.android.core.di.WorkerKey
import com.metasearch.android.core.di.scope.WorkerScope
import com.metasearch.android.core.worker.api.constants.ModelDownloadKeys
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        val version = inputData.getString(ModelDownloadKeys.KEY_MODEL_COMMIT_HASH) ?: "v1"
        val fileName = inputData.getString(ModelDownloadKeys.KEY_MODEL_DOWNLOAD_FILE_NAME)
        val modelDir = inputData.getString(ModelDownloadKeys.KEY_MODEL_DOWNLOAD_MODEL_DIR) ?: "models"
        val isZip = inputData.getBoolean(ModelDownloadKeys.KEY_MODEL_IS_ZIP, false)
        val unzippedDir = inputData.getString(ModelDownloadKeys.KEY_MODEL_UNZIPPED_DIR)

        if (fileUrl == null || fileName == null) {
            return@withContext Result.failure()
        }

        try {
            val externalFilesDir = applicationContext.getExternalFilesDir(null)

            val outputDir = File(externalFilesDir, listOf(modelDir, version).joinToString(File.separator))
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }

            val outputTmpFile = File(outputDir, "$fileName.$TMP_FILE_EXT")
            val outputFileBytes = outputTmpFile.length()

            Log.d(TAG, "Requesting URL: $fileUrl")
            val url = URL(fileUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")

            val accessToken = inputData.getString("KEY_MODEL_DOWNLOAD_ACCESS_TOKEN")

            if (!accessToken.isNullOrEmpty()) {
                connection.setRequestProperty("Authorization", "Bearer $accessToken")
            }

            connection.connectTimeout = 30_000
            connection.readTimeout = 600_000

            if (outputFileBytes > 0) {
                Log.d(TAG, "Partial file found. Trying to resume download from $outputFileBytes bytes.")
                connection.setRequestProperty("Range", "bytes=${outputFileBytes}-")
                connection.setRequestProperty("Accept-Encoding", "identity")
            }
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK &&
                connection.responseCode != HttpURLConnection.HTTP_PARTIAL) {
                throw IOException("HTTP error code: ${connection.responseCode}")
            }

            val totalBytes = inputData.getLong(ModelDownloadKeys.KEY_MODEL_TOTAL_BYTES, 2253150000L)
            var bytesWritten = outputFileBytes

            val inputStream = connection.inputStream
            val outputStream = FileOutputStream(outputTmpFile, true).use { outputStream ->
                val buffer = ByteArray(8192)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    bytesWritten += bytesRead

                    val progressPercent = ((bytesWritten.toFloat() / totalBytes) * 100f)
                    setProgressAsync(Data.Builder().putFloat("KEY_PROGRESS", progressPercent).build())
                }
            }
            inputStream.close()

            val originalFile = File(outputDir, fileName)
            if (originalFile.exists()) {
                originalFile.delete()
            }

            if (!outputTmpFile.renameTo(originalFile)) {
                Log.e(TAG, "Rename failed! Check disk space or path permissions.")
                return@withContext Result.failure()
            }
            Log.d(TAG, "Download completed successfully.")

            if (isZip && unzippedDir != null) {
                val destDir = File(outputDir, unzippedDir)
                if (!destDir.exists()) {
                    destDir.mkdirs()
                }

                val unzipBuffer = ByteArray(4096)
                val zipIn = ZipInputStream(BufferedInputStream(FileInputStream(originalFile)))
                var zipEntry: ZipEntry? = zipIn.nextEntry

                while (zipEntry != null) {
                    val filePath = destDir.absolutePath + File.separator + zipEntry.name
                    if (!zipEntry.isDirectory) {
                        FileOutputStream(filePath).use { bos ->
                            var len: Int
                            while (zipIn.read(unzipBuffer).also { len = it } > 0) {
                                bos.write(unzipBuffer, 0, len)
                            }
                        }
                    } else {
                        File(filePath).mkdirs()
                    }
                    zipIn.closeEntry()
                    zipEntry = zipIn.nextEntry
                }
                zipIn.close()

                originalFile.delete()
                Log.d(TAG, "Unzipping completed and cleanup done.")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Download failed", e)
            Result.failure()
        }
    }
}
