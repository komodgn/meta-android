package com.metasearch.android.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

abstract class ApiAbstract<T> {

    lateinit var mockWebServer: MockWebServer

    private val jsonRule = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @BeforeEach
    fun createMockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @AfterEach
    fun stopServer() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("response$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8)),
        )
    }

    fun createService(clazz: Class<T>): T {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(jsonRule.asConverterFactory(contentType))
            .build()
            .create(clazz)
    }
}
