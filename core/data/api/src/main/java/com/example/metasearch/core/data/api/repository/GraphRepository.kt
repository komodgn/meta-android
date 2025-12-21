package com.example.metasearch.core.data.api.repository

import android.net.Uri

interface GraphRepository {
    suspend fun getFullGraphWebViewUrl(): String
    suspend fun getDetailGraphWebViewUrl(imageUriString: String): String

    suspend fun getTripleData(photoName: String)

    /**
     * @return 서버에서 받은 파일명을 기반으로 찾은 Uri
     */
    suspend fun findMatchedUri(photoName: String): Uri?
}
