package com.metasearch.android.data.remote

import com.google.common.truth.Truth.assertThat
import com.metasearch.android.data.remote.search.request.Circle
import com.metasearch.android.data.remote.search.request.FocusingSearchRequest
import com.metasearch.android.data.remote.search.service.SearchAIService
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Before
import org.junit.Test

class SearchAIServiceTest : ApiAbstract<SearchAIService>() {

    private lateinit var service: SearchAIService

    @Before
    fun initService() {
        service = createService(SearchAIService::class.java)
    }

    @Test
    fun `uploadImageAndCircles sends multipart request and parses response successfully`() = runTest {
        // given
        enqueueResponse("/CircleDetectionResponse.json")
        val imagePart = MultipartBody.Part.createFormData(
            "searchImage",
            "test_image.jpeg",
            "fake_image_content".toRequestBody("image/jpeg".toMediaType()),
        )
        val dbNamePart = "dbabf1e5c83b8b485da6ba3b94ebf78dgs".toRequestBody("text/plain".toMediaType())
        val mockRequest = FocusingSearchRequest(
            circles = listOf(Circle(centerX = 0.5f, centerY = 0.5f, radius = 5f)),
        )

        // when
        val response = service.uploadImageAndCircles(
            image = imagePart,
            dbName = dbNamePart,
            request = mockRequest,
        )

        // then
        val recordedRequest = mockWebServer.takeRequest()

        assertThat(recordedRequest.method).isEqualTo("POST")
        assertThat(recordedRequest.path).isEqualTo("/android/circle_search")
        assertThat(recordedRequest.getHeader("Content-Type")).contains("multipart/form-data")

        assertThat(response.message).isEqualTo("success")
        assertThat(response.detectedObjects).hasSize(2)
        assertThat(response.detectedObjects).containsExactly("고양이", "안경")
    }
}
