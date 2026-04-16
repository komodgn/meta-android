package com.metasearch.android.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

abstract class ApiAbstract<T> {

    lateinit var mockWebServer: MockWebServer

    private val jsonRule = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Before
    fun createMockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val resourcePath = "response/${fileName.removePrefix("/")}"
        val body = requireNotNull(javaClass.classLoader?.getResourceAsStream(resourcePath)) {
            "Fixture not found: $resourcePath"
        }.use { inputStream ->
            inputStream.source().buffer().use { source ->
                source.readString(Charsets.UTF_8)
            }
        }
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(body),
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
