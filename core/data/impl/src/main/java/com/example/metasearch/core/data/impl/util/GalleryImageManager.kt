package com.example.metasearch.core.data.impl.util

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

internal object GalleryImageManager {

    /**
     * @return 갤러리 이미지 실제 경로
      */
    fun getAllGalleryImagesUriToString(context: Context): List<String> {
        val imagePaths = mutableListOf<String>()
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                imagePaths.add(cursor.getString(dataColumn))
            }
        }
        return imagePaths
    }

    /**
     * @return 갤러리 이미지 URI 리스트 가져오기 (JPEG, PNG 필터링)
      */
    fun getAllGalleryImagesUri(context: Context): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.MIME_TYPE}=? OR ${MediaStore.Images.Media.MIME_TYPE}=?"
        val selectionArgs = arrayOf("image/jpeg", "image/png")

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, selection, selectionArgs, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                imageUris.add(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id))
            }
        }
        return imageUris
    }

    /**
     * @return 이름(DisplayName) -> Uri 매핑 정보
      */
    fun getAllGalleryImagesUriWithName(context: Context): Map<String, Uri> {
        val images = mutableMapOf<String, Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                images[name] = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            }
        }
        return images
    }

    /**
     * @return 확장자 제외한 모든 파일 이름 리스트
      */
    fun getAllImageNamesWithoutExtension(context: Context): List<String> {
        val names = mutableListOf<String>()
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )?.use { cursor ->
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val fullName = cursor.getString(nameColumn)
                val nameWithoutExtension = fullName.substringBeforeLast('.', fullName)
                names.add(nameWithoutExtension)
            }
        }
        return names
    }

    /**
     * @return 서버 파일명 리스트와 매칭되는 URI 리스트
      */
    fun findMatchedUris(photoNamesFromServer: List<String>, context: Context): List<Uri> {
        val allImages = getAllGalleryImagesUriWithName(context)
        return photoNamesFromServer.mapNotNull { name -> allImages[name] }
    }

    /**
     * @return 단일 파일명과 매칭되는 URI
     */
    fun findMatchedUri(photoNameFromServer: String, context: Context): Uri? {
        return getAllGalleryImagesUriWithName(context)[photoNameFromServer]
    }

    /**
     * @return URI를 통해 찾은 파일 이름
      */
    fun getFileNameFromUri(context: Context, imageUri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        return context.contentResolver.query(imageUri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            } else null
        }
    }

    /**
     * @return Bitmap을 Byte 배열로 변환한 값
      */
    fun getBytes(bitmap: Bitmap): ByteArray {
        return ByteArrayOutputStream().use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.toByteArray()
        }
    }
}
