package com.metasearch.android.data.remote

import com.google.common.truth.Truth.assertThat
import com.metasearch.android.data.remote.person.request.PersonFrequencyRequest
import com.metasearch.android.data.remote.person.service.PersonWebService
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.BeforeEach

class PersonWebServiceTest : ApiAbstract<PersonWebService>() {

    private lateinit var service: PersonWebService

    @BeforeEach
    fun initService() {
        service = createService(PersonWebService::class.java)
    }

    @Test
    fun `getPersonFrequency parses response correctly`() = runTest {
        // given
        enqueueResponse("/PersonFrequencyResponse.json")
        val mockRequest = PersonFrequencyRequest(
            dbName = "dbabf1e5c83b8b485da6ba3b94ebf78dgs",
            personNames = listOf("홍철", "지수"),
        )

        // when
        val response = service.getPersonFrequency(mockRequest)

        // then
        assertThat(response.frequencies).hasSize(2)

        with(response.frequencies[0]) {
            assertThat(personName).isEqualTo("홍철")
            assertThat(frequency).isEqualTo(15)
        }

        with(response.frequencies[1]) {
            assertThat(personName).isEqualTo("지수")
            assertThat(frequency).isEqualTo(8)
        }
    }
}
