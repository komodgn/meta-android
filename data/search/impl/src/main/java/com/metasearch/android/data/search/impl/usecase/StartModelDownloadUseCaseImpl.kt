package com.metasearch.android.data.search.impl.usecase

import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.core.worker.api.constants.ModelDownloadKeys
import com.metasearch.android.core.worker.api.constants.WorkerNames
import com.metasearch.android.core.worker.api.model.WorkOptions
import com.metasearch.android.core.worker.api.usecase.WorkScheduleUseCase
import com.metasearch.android.data.domain.Model
import com.metasearch.android.data.remote.llm.worker.ModelDownloadWorker
import com.metasearch.android.domain.search.api.usecase.StartModelDownloadUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class StartModelDownloadUseCaseImpl(
    private val workScheduleUseCase: WorkScheduleUseCase,
) : StartModelDownloadUseCase {

    override fun invoke(model: Model) {
        model.preProcess()

        val safeTotalBytes = if (model.totalBytes > 0) model.totalBytes else 1024L * 1024 * 1024

        workScheduleUseCase.scheduleNow(
            workName = WorkerNames.GLOBAL_MODEL_DOWNLOAD,
            klass = ModelDownloadWorker::class,
            options = WorkOptions(
                networkRequirement = NetworkType.CONNECTED,
                requiresCharging = false,
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
            ),
            params = {
                putString(ModelDownloadKeys.KEY_MODEL_NAME, model.name)
                putString(ModelDownloadKeys.KEY_MODEL_URL, model.downloadUrl)
                putString(ModelDownloadKeys.KEY_MODEL_COMMIT_HASH, model.version)
                putString(ModelDownloadKeys.KEY_MODEL_DOWNLOAD_FILE_NAME, model.downloadFileName)
                putString(ModelDownloadKeys.KEY_MODEL_DOWNLOAD_MODEL_DIR, model.normalizedName)
                putBoolean(ModelDownloadKeys.KEY_MODEL_IS_ZIP, model.isZip)
                putString(ModelDownloadKeys.KEY_MODEL_UNZIPPED_DIR, model.unzipDir)
                putLong(ModelDownloadKeys.KEY_MODEL_TOTAL_BYTES, safeTotalBytes)
            },
        )
    }
}
