package com.metasearch.android.data.search.impl.mapper

import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.data.domain.Model
import com.metasearch.android.data.domain.ModelDataFile
import com.metasearch.android.data.domain.PhotoGroup
import com.metasearch.android.data.remote.llm.response.ModelResponse
import com.metasearch.android.data.remote.search.response.PhotoNameResponse
import com.metasearch.android.data.remote.search.response.PhotoResponse

internal fun PhotoResponse.toModel(): DragSearchResult {
    val commonList = photos.commonPhotos.distinct()
    val individualMap = photos.individualPhotos
    val resultGroups = mutableListOf<PhotoGroup>()

    if (commonList.isNotEmpty()) {
        val relatedCategories = individualMap.filter { entry ->
            entry.value.any { it in commonList }
        }.keys.joinToString(", ")

        resultGroups.add(PhotoGroup(categoryName = relatedCategories, photoNames = commonList))
    }

    individualMap.forEach { (category, photoNames) ->
        val filteredNames = photoNames.filter { it !in commonList }

        if (filteredNames.isNotEmpty()) {
            resultGroups.add(
                PhotoGroup(
                    categoryName = category,
                    photoNames = filteredNames,
                ),
            )
        }
    }

    return DragSearchResult(groups = resultGroups)
}

internal fun PhotoNameResponse.toModel(): List<String> {
    return this.photoNames
}

internal fun ModelResponse.toModel(): Model {
    return Model(
        name = this.name,
        modelId = this.modelId,
        displayName = this.name,
        sizeInBytes = this.sizeInBytes,
        downloadFileName = this.modelFile,
        version = this.commitHash,
        extraDataFiles = this.updatableModelFiles.map {
            ModelDataFile(
                name = it.fileName,
                url = "https://huggingface.co/${this.modelId}/resolve/${this.commitHash}/${it.fileName}?download=true",
                downloadFileName = it.fileName,
                sizeInBytes = 0L,
            )
        },
        isZip = this.modelFile.endsWith(".zip"),
        unzipDir = "",
        localFileRelativeDirPathOverride = "",
        localModelFilePathOverride = "",
        imported = false,
    )
}

internal fun List<ModelResponse>.toModelList(): List<Model> {
    return this.map { it.toModel() }
}
