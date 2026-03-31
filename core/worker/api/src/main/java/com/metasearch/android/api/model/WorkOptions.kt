package com.metasearch.android.api.model

import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType

data class WorkOptions(
    val existingWorkPolicy: ExistingWorkPolicy = ExistingWorkPolicy.REPLACE,
    val networkRequirement: NetworkType = NetworkType.CONNECTED,
    val requiresCharging: Boolean = false,
)
