package com.metasearch.android.data.domain

data class GalleryImage(
    val id: Long, // MediaStore._ID
    val uriString: String,
    val dateAdded: Long,
) {
    companion object
}

fun GalleryImage.Companion.fake(id: Long = 1L): GalleryImage = GalleryImage(
    id = id,
    uriString = "https://picsum.photos/seed/${id + 100}/200/200",
    dateAdded = System.currentTimeMillis(),
)

fun GalleryImage.Companion.fakes(count: Int = 100): List<GalleryImage> = (1..count).map {
    fake(it.toLong())
}
