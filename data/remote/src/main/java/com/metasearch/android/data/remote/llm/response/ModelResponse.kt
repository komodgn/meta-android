package com.metasearch.android.data.remote.llm.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelListResponse(
    @SerialName("models") val models: List<ModelResponse>
)

@Serializable
data class ModelResponse(
    @SerialName("name") val name: String,
    @SerialName("modelId") val modelId: String,
    @SerialName("modelFile") val modelFile: String,
    @SerialName("description") val description: String,
    @SerialName("sizeInBytes") val sizeInBytes: Long,
    @SerialName("minDeviceMemoryInGb") val minDeviceMemoryInGb: Int,
    @SerialName("commitHash") val commitHash: String,
    @SerialName("llmSupportImage") val llmSupportImage: Boolean,
    @SerialName("llmSupportAudio") val llmSupportAudio: Boolean,
    @SerialName("capabilities") val capabilities: List<String>,
    @SerialName("defaultConfig") val defaultConfig: DefaultConfigResponse,
    @SerialName("taskTypes") val taskTypes: List<String>,
    @SerialName("bestForTaskTypes") val bestForTaskTypes: List<String>,
    @SerialName("capabilityToTaskTypes") val capabilityToTaskTypes: Map<String, List<String>>,
    @SerialName("updatableModelFiles") val updatableModelFiles: List<ModelFileResponse>,
    @SerialName("updateInfo") val updateInfo: String
)

@Serializable
data class DefaultConfigResponse(
    @SerialName("topK") val topK: Int,
    @SerialName("topP") val topP: Double,
    @SerialName("temperature") val temperature: Double,
    @SerialName("maxContextLength") val maxContextLength: Int,
    @SerialName("maxTokens") val maxTokens: Int,
    @SerialName("accelerators") val accelerators: String,
    @SerialName("visionAccelerator") val visionAccelerator: String
)

@Serializable
data class ModelFileResponse(
    @SerialName("fileName") val fileName: String,
    @SerialName("commitHash") val commitHash: String
)
