package com.metasearch.android.data.analysis.impl.mapper

import android.util.Base64
import android.util.Base64.decode
import com.metasearch.android.core.network.response.UploadResponse
import com.metasearch.android.data.domain.AnalysisResult
import com.metasearch.android.data.domain.DetectedPerson

internal fun UploadResponse.toModel(): AnalysisResult = AnalysisResult(
    detectedPersons = images.map { person ->
        DetectedPerson(
            imageName = person.imageName,
            imageBytes = person.imageBytes?.let {
                runCatching { decode(it, Base64.DEFAULT) }.getOrNull()
            },
            isFaceExist = person.isFaceExit,
        )
    },
)
