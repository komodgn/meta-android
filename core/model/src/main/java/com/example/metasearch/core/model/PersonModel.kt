package com.example.metasearch.core.model

import androidx.compose.runtime.Stable
import java.util.Objects

@Stable
data class PersonModel(
    var id: Int,

    var imageName: String,

    var image: ByteArray,

    var inputName: String = "",

    var phone: String = "",

    var homeDisplay: Boolean = false,

    var photoCount: Int = 0,

    var totalDuration: Long = 0,

    var normalizedScore: Double = 0.0,

    var thumbnailImage: ByteArray? = null,
) {
    init {
        this.phone = phone
    }

    private fun areThumbnailsEqual(other: PersonModel): Boolean {
        if (thumbnailImage != null) {
            if (other.thumbnailImage == null) return false
            return thumbnailImage.contentEquals(other.thumbnailImage)
        } else {
            return other.thumbnailImage == null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PersonModel

        if (id != other.id) return false
        if (imageName != other.imageName) return false
        if (inputName != other.inputName) return false
        if (!image.contentEquals(other.image)) return false
        if (phone != other.phone) return false
        if (homeDisplay != other.homeDisplay) return false
        if (photoCount != other.photoCount) return false
        if (totalDuration != other.totalDuration) return false
        if (normalizedScore != other.normalizedScore) return false

        if (!areThumbnailsEqual(other)) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id, imageName, inputName, image.contentHashCode(), phone, homeDisplay,
            photoCount, totalDuration, normalizedScore, thumbnailImage.contentHashCode(),
        )
    }
}
