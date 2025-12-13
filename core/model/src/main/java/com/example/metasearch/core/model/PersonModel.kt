package com.example.metasearch.core.model

import androidx.compose.runtime.Stable

@Stable
data class PersonModel(
    val id: Long,
    val name: String,
    val frequency: Int,
    val profileImageUrl: String? = null,
)
