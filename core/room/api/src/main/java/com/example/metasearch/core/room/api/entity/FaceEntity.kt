package com.example.metasearch.core.room.api.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "faces",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["person_id"],
            onDelete = CASCADE,
        ),
    ],
    indices = [Index(value = ["person_id"])],
)
data class FaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "person_id")
    val personId: Long,

    @ColumnInfo(name = "image_name")
    val imageName: String,

    @ColumnInfo(name = "image_data")
    val imageData: ByteArray,

    @ColumnInfo(name = "thumbnail_data")
    val thumbnailData: ByteArray? = null,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
) {
    private fun areThumbnailsEqual(other: FaceEntity): Boolean {
        if (thumbnailData != null) {
            if (other.thumbnailData == null) return false
            return thumbnailData.contentEquals(other.thumbnailData)
        } else {
            return other.thumbnailData == null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FaceEntity

        if (id != other.id) return false
        if (personId != other.personId) return false
        if (imageName != other.imageName) return false
        if (!imageData.contentEquals(other.imageData)) return false

        if (!areThumbnailsEqual(other)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + personId.hashCode()
        result = 31 * result + imageName.hashCode()
        result = 31 * result + imageData.contentHashCode()
        result = 31 * result + (thumbnailData?.contentHashCode() ?: 0)

        return result
    }
}
