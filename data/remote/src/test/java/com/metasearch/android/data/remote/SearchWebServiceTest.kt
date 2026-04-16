package com.metasearch.android.data.remote

import com.google.common.truth.Truth.assertThat
import com.metasearch.android.data.remote.search.request.DetectedObjectsRequest
import com.metasearch.android.data.remote.search.request.NLQueryRequest
import com.metasearch.android.data.remote.search.service.SearchWebService
import com.metasearch.android.data.remote.search.util.CypherQueryGenerator.generateQueryByKeywords
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchWebServiceTest : ApiAbstract<SearchWebService>() {

    private lateinit var service: SearchWebService

    @Before
    fun initService() {
        service = createService(SearchWebService::class.java)
    }

    @Test
    fun `sendDetectedObjects - parses response successfully`() = runTest {
        // given
        enqueueResponse("/PhotoResponse.json")

        // when
        val response = service.sendDetectedObjects(
            DetectedObjectsRequest(
                dbName = "dbabf1e5c83b8b485da6ba3b94ebf78dgs",
                properties = listOf("안경", "모자", "남자"),
            ),
        )

        // then
        assertThat(response.photos).isNotNull()

        assertThat(response.photos.commonPhotos).hasSize(2)
        assertThat(response.photos.commonPhotos).containsExactly("common_1.jpg", "common_2.jpg")

        assertThat(response.photos.individualPhotos).containsKey("안경")
        assertThat(response.photos.individualPhotos["안경"]).contains("glass_1.jpg")

        assertThat(response.photos.individualPhotos).containsKey("모자")
        assertThat(response.photos.individualPhotos["모자"]).hasSize(1)
    }

    @Test
    fun `sendCypherQuery - parses PhotoNameResponse successfully`() = runTest {
        // given
        enqueueResponse("/PhotoNameResponse.json")

        val keywords = listOf("안경", "모자")
        val generatedQuery = generateQueryByKeywords(keywords)

        val mockRequest = NLQueryRequest(
            dbName = "dbabf1e5c83b8b485da6ba3b94ebf78dgs",
            query = generatedQuery,
        )

        // when
        val response = service.sendCypherQuery(mockRequest)

        // then
        assertThat(response.photoNames).isNotNull()
        assertThat(response.photoNames).hasSize(3)
        assertThat(response.photoNames).containsExactly("photo_001.jpg", "photo_002.jpg", "photo_003.jpg")
    }
}
