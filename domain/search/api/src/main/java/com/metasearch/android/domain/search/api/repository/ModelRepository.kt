package com.metasearch.android.domain.search.api.repository

import com.metasearch.android.data.domain.Model

interface ModelRepository {
    fun getModel(name: String): Model?
    fun getAllModels(): List<Model>
    fun getLocalFilePath(model: Model, basePath: String, fileName: String): String
}
