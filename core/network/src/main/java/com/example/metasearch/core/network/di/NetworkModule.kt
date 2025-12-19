package com.example.metasearch.core.network.di

import com.example.metasearch.core.network.BuildConfig
import com.example.metasearch.core.network.service.AIService
import com.example.metasearch.core.network.service.OpenAIService
import com.example.metasearch.core.network.service.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    private const val MAX_TIMEOUT_MILLIS = 20_000L
    private const val MAX_TIMEOUT_SECONDS_AI = 6000L
    private const val OPENAI_SERVER_BASE_URL = "https://api.openai.com/"

    private val jsonRule = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    private val jsonConverterFactory = jsonRule.asConverterFactory("application/json".toMediaType())

    @Singleton
    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .connectTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Named("AIOkHttpClient")
    @Singleton
    @Provides
    internal fun provideAIOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .connectTimeout(MAX_TIMEOUT_SECONDS_AI, TimeUnit.SECONDS)
            .readTimeout(MAX_TIMEOUT_SECONDS_AI, TimeUnit.SECONDS)
            .writeTimeout(MAX_TIMEOUT_SECONDS_AI, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideWebRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEB_SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideWebService(webRetrofit: Retrofit): WebService {
        return webRetrofit.create(WebService::class.java)
    }

    @Singleton
    @Provides
    internal fun provideAIRetrofit(
        @Named("AIOkHttpClient") okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.AI_SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideAIService(aiRetrofit: Retrofit): AIService {
        return aiRetrofit.create(AIService::class.java)
    }

    @Named("OpenAIOkHttpClient")
    @Singleton
    @Provides
    internal fun provideOpenAIOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Singleton
    @Provides
    internal fun provideOpenAIRetrofit(
        @Named("OpenAIOkHttpClient") okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OPENAI_SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideOpenAIService(openAIRetrofit: Retrofit): OpenAIService {
        return openAIRetrofit.create(OpenAIService::class.java)
    }
}
