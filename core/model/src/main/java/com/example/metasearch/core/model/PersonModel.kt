package com.example.metasearch.core.model

import androidx.compose.runtime.Stable

@Stable
data class PersonModel(
    val id: Long,
    val name: String, // 시스템 부여 이름
    val inputName: String, // 사용자 입력 이름 (초기값은 name과 동일)
    val phoneNumber: String = "",
    val isHomeDisplay: Boolean = false,
    val photoCount: Int = 0,
    val totalDuration: Long = 0,
    val normalizedScore: Double = 0.0,
    // 1:N 관계 반영
    val faces: List<FaceModel> = emptyList(),
)

@Stable
data class FaceModel(
    val id: Long,
    val personId: Long,
    val imageName: String,
    val imageData: ByteArray,
    val phoneNumber: String = "",
)
