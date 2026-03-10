package com.metasearch.android.feature.home.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.data.api.repository.ImageAnalysisRepository
import com.metasearch.android.core.notification.notifier.AnalysisNotifier
import com.metasearch.android.feature.home.R
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class ImageAnalysisWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    private val entryPoint = EntryPoints.get(applicationContext, AnalysisEntryPoint::class.java)
    private val repository = entryPoint.repository()
    private val notifier = entryPoint.notifier()

    override suspend fun doWork(): Result {
        return try {
            repository.runFullAnalysis()

            notifier.notifyComplete(
                UiText.StringResource(R.string.notification_analysis_complete_title),
                UiText.StringResource(R.string.notification_analysis_complete_content),
            )

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AnalysisEntryPoint {
        fun repository(): ImageAnalysisRepository
        fun notifier(): AnalysisNotifier
    }
}

// @HiltWorker
// class ImageAnalysisWorker @AssistedInject constructor(
//     @Assisted context: Context,
//     @Assisted params: WorkerParameters,
//     private val imageAnalysisRepository: ImageAnalysisRepository,
// ) : CoroutineWorker(context, params) {
//
//     override suspend fun doWork(): Result {
//         return try {
//             imageAnalysisRepository.runFullAnalysis()
//
//             val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>().build()
//
//             WorkManager.getInstance(applicationContext).enqueue(notificationWork)
//             Result.success()
//         } catch (e: Exception) {
//             Result.failure()
//         }
//     }
// }
