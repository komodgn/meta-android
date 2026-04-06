package com.metasearch.android.data.domain

import kotlin.collections.find
import kotlin.collections.firstOrNull

data class Person(
    val id: Long,
    val name: String, // Server label
    val inputName: String,
    val phoneNumber: String = "",
    val isHomeDisplay: Boolean = false,
    val photoCount: Int = 0,
    val totalDuration: Long = 0,
    val normalizedScore: Double = 0.0,
    val representativeFaceId: Long? = null,
    val faces: List<Face> = emptyList(),
) {
    val representativeFace: Face?
        get() = faces.find { it.id == representativeFaceId } ?: faces.firstOrNull()

    companion object
}

fun Person.Companion.fake(id: Long = 1L): Person {
    return Person(
        id = id,
        name = "인물${id.toInt()}",
        inputName = "라이언",
        faces = Face.fakes(),
    )
}

fun Person.Companion.fakes() = (1..10)
    .map { fake(it.toLong()) }
