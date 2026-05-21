package com.metasearch.android.data.search.impl.repository

import android.content.Context
import android.util.Log
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.Model
import com.metasearch.android.data.remote.llm.response.ModelListResponse
import com.metasearch.android.data.search.impl.mapper.toModelList
import com.metasearch.android.domain.search.api.repository.ModelRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json

@SingleIn(DataScope::class)
@Inject
class ModelRepositoryImpl(
    private val context: Context,
) : ModelRepository {

    private val models: List<Model> by lazy {
        loadModelsFromJson()
    }

    private fun loadModelsFromJson(): List<Model> {
        return try {
            val jsonString = context.assets.open("model.json")
                .bufferedReader().use { it.readText() }
            val json = Json { ignoreUnknownKeys = true }
            val wrapper = json.decodeFromString<ModelListResponse>(jsonString)

            return wrapper.models.toModelList()
        } catch (e: Exception) {
            Log.e("ModelRepositoryImpl", "JSON parsing or file loading error", e)
            emptyList()
        }
    }

    override fun getModel(name: String): Model? = models.find { it.name == name }

    override fun getAllModels(): List<Model> = models

    override fun getLocalFilePath(model: Model, basePath: String, fileName: String): String {
        return model.getPath(basePath, fileName)
    }
}
