package com.metasearch.android.feature.home.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.metasearch.android.core.common.utils.UiText
import com.metasearch.android.core.data.api.repository.ImageAnalysisRepository
import com.metasearch.android.core.di.ChildWorkerFactory
import com.metasearch.android.core.di.WorkerKey
import com.metasearch.android.core.notification.notifier.AnalysisNotifier
import com.metasearch.android.feature.home.R
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap

@AssistedInject
class ImageAnalysisWorker(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ImageAnalysisRepository,
    private val notifier: AnalysisNotifier,
) : CoroutineWorker(context, params) {

    @AssistedFactory
    @ContributesIntoMap(AppScope::class)
    @WorkerKey(ImageAnalysisWorker::class)
    interface Factory : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ImageAnalysisWorker
    }

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
}
