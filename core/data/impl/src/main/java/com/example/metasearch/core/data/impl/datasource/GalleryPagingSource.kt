package com.example.metasearch.core.data.impl.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.metasearch.core.model.GalleryImageModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GalleryPagingSource(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : PagingSource<Int, GalleryImageModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryImageModel> {
        return withContext(ioDispatcher) {
            try {
                val offset = params.key ?: 0
                val limit = params.loadSize
                val imageList = mutableListOf<GalleryImageModel>()

                val projection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATE_ADDED,
                )

                val queryArgs = android.os.Bundle().apply {
                    putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                    putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Images.Media.DATE_ADDED))
                    putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
                }

                context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    queryArgs,
                    null
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val date = cursor.getLong(dateColumn)
                        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        imageList.add(GalleryImageModel(id = id, uriString = uri.toString(), dateAdded = date))
                    }
                }

                LoadResult.Page(
                    data = imageList,
                    prevKey = if (offset == 0) null else offset - limit,
                    nextKey = if (imageList.size < limit) null else offset + imageList.size
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GalleryImageModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }
}
