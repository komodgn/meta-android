package com.metasearch.android.data.search.impl.repository

import android.util.Log
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.annotation.IoDispatcher
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.Circle
import com.metasearch.android.data.domain.DragSearchResult
import com.metasearch.android.data.domain.Model
import com.metasearch.android.data.remote.search.SearchClient
import com.metasearch.android.data.remote.search.constant.PromptConstants
import com.metasearch.android.data.remote.search.util.CypherQueryGenerator
import com.metasearch.android.data.search.impl.mapper.toModel
import com.metasearch.android.domain.file.api.repository.FileRepository
import com.metasearch.android.domain.search.api.repository.ModelRepository
import com.metasearch.android.domain.search.api.repository.SearchRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "SearchRepo"

@SingleIn(DataScope::class)
@Inject
class SearchRepositoryImpl(
    private val searchClient: SearchClient,
    private val fileRepository: FileRepository,
    private val modelRepository: ModelRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SearchRepository {

    override suspend fun analyzeFocusingImage(
        dbName: String,
        imageFile: File,
        circles: List<Circle>,
    ): List<String> = runSuspendCatching {
        val response = searchClient.analyzeFocusingImage(dbName, imageFile, circles)
        response.detectedObjects
    }.getOrDefault(emptyList())

    override suspend fun extractKeywordsFromNL(query: String): List<String> = runSuspendCatching {
        searchClient.extractKeywords(query)
    }.getOrDefault(emptyList())

    override suspend fun extractKeywordsFromLocalNL(query: String): List<String> = withContext(ioDispatcher) {
        runSuspendCatching {
            val model = checkNotNull(modelRepository.getModel("Gemma-4-E2B-it")) {
                "Model not found"
            }
            // Retrieve the path where the model was unzipped by the Worker
            // Location structure: externalFilesDir + modelDir + version + unzippedDir
            val externalFilesDir = fileRepository.getExternalFile("").absolutePath
            val modelBaseDir = File(
                externalFilesDir,
                listOf(model.normalizedName, model.version).joinToString(File.separator),
            )
            val unzippedModelPath = File(modelBaseDir, model.unzipDir).absolutePath
            val modelFile = File(unzippedModelPath, model.downloadFileName)
            Log.d(TAG, "DEBUG: Checking file at: ${modelFile.absolutePath}")
            Log.d(TAG, "DEBUG: File exists? ${modelFile.exists()}")
            if (!modelFile.exists()) {
                Log.e(TAG, "ERROR: Model file not found at: ${modelFile.absolutePath}")
                return@runSuspendCatching emptyList()
            }
            try {
                val engineConfig = EngineConfig(
                    modelPath = modelFile.absolutePath,
                    backend = Backend.GPU(),
                    maxNumTokens = 512,
                )

            var engine: Engine? = null
            try {
                val engineConfig = EngineConfig(
                    modelPath = modelFile.absolutePath,
                    backend = Backend.GPU(),
                    maxNumTokens = 512,
                )

                engine = Engine(engineConfig)
                engine.initialize()

                Log.d(TAG, "DEBUG: Creating Conversation...")
                val conversation = engine.createConversation()

                Log.d(TAG, "DEBUG: Sending Message: $query")
                val response = conversation.sendMessage(PromptConstants.NL_SEARCH_BASIC_PROMPT + query)

                val rawResult = response?.toString() ?: "NULL_RESPONSE"
                Log.d(TAG, "DEBUG: Raw Result: '$rawResult'")

                return@runSuspendCatching rawResult.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            } catch (e: Exception) {
                Log.e(TAG, "ERROR: Exception during inference", e)
                return@runSuspendCatching emptyList()
            } finally {
                engine?.close()
            }
        }.getOrDefault(emptyList())
    }

    override fun isLocalModelAvailable(model: Model): Boolean {
        val model = modelRepository.getModel(model.name) ?: return false
        Log.d(TAG, model.name)
        val path = modelRepository.getLocalFilePath(
            model,
            fileRepository.getExternalFile("").absolutePath,
            model.downloadFileName,
        )
        val file = File(path)
        Log.d(TAG, "file: $path")
        return file.exists() && file.length() > 0
    }

    override suspend fun findPhotosByDetectedObjects(
        dbName: String,
        properties: List<String>,
    ): DragSearchResult = runSuspendCatching {
        val response = searchClient.fetchPhotosByObjects(dbName, properties)
        response.toModel()
    }.getOrDefault(DragSearchResult(emptyList()))

    override suspend fun searchPhotosByKeywords(dbName: String, keywords: List<String>): List<String> = runSuspendCatching {
        val neo4jquery = CypherQueryGenerator.generateQueryByKeywords(keywords)
        val response = searchClient.fetchPhotosByKeywords(dbName, neo4jquery)
        response.toModel()
    }.getOrDefault(emptyList())
}
