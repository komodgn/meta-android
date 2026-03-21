package com.metasearch.android.core.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlin.collections.find
import kotlin.collections.firstOrNull

@Immutable
data class Person(
    val id: Long,
    val name: String, // 시스템 부여 이름
    val inputName: String, // 사용자 입력 이름 (초기값은 name과 동일)
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

fun Person.Companion.fakes(): PersistentList<Person> = (1..10)
    .map { fake(it.toLong()) }
    .toPersistentList()
