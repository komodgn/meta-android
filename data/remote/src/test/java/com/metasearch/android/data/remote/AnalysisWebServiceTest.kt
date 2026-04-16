package com.metasearch.android.data.remote

import com.google.common.truth.Truth.assertThat
import com.metasearch.android.data.remote.analysis.service.AnalysisWebService
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AnalysisWebServiceTest : ApiAbstract<AnalysisWebService>() {

    private lateinit var service: AnalysisWebService

    @Before
    fun initService() {
        service = createService(AnalysisWebService::class.java)
    }

    @Test
    fun `fetchTripleData - parses response successfully`() = runTest {
        // given
        enqueueResponse("/TripleResponse.json")

        // when
        val response = service.fetchTripleData("dbabf1e5c83b8b485da6ba3b94ebf78dgs", "photo_1.jpg")

        // then
        val keywords = response.triple.split(",").map { it.trim() }

        assertThat(keywords).hasSize(3)
        assertThat(keywords).containsExactly("밤", "여름", "음식")
    }
}
