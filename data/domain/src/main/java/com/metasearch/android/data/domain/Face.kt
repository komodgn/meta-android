package com.metasearch.android.data.domain

import androidx.compose.runtime.Immutable

@Immutable
data class Face(
    val id: Long,
    val personId: Long,
    val imageName: String,
    val imageData: ByteArray,
    val phoneNumber: String = "",
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Face) return false
        if (id != other.id) return false
        if (!imageData.contentEquals(other.imageData)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageData.contentHashCode()
        return result
    }

    companion object
}

fun Face.Companion.fake(id: Long = 1L): Face {
    return Face(
        id = id,
        personId = 1L,
        imageName = "인물${id.toInt()}",
        imageData = ByteArray(
            size = 3,
        ),
    )
}

fun Face.Companion.fakes() = (1..20)
    .map { fake(it.toLong()) }
