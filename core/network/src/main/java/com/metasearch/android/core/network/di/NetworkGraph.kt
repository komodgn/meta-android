package com.metasearch.android.core.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.metasearch.android.core.network.BuildConfig
import com.metasearch.android.core.network.service.AIService
import com.metasearch.android.core.network.service.OpenAIService
import com.metasearch.android.core.network.service.WebService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private const val MAX_TIMEOUT_MILLIS = 20_000L
private const val MAX_TIMEOUT_SECONDS_AI = 6000L
private const val OPENAI_SERVER_BASE_URL = "https://api.openai.com/"

private val jsonRule = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
}
private val jsonConverterFactory = jsonRule.asConverterFactory("application/json".toMediaType())

@ContributesTo(AppScope::class)
interface NetworkGraph {

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
    }

    @Provides
    fun provideChuckerInterceptor(
        context: Context,
    ): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Named("AIOkHttpClient")
    @SingleIn(AppScope::class)
    @Provides
    fun provideAIOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(MAX_TIMEOUT_SECONDS_AI, TimeUnit.SECONDS)
            .readTimeout(MAX_TIMEOUT_SECONDS_AI, TimeUnit.SECONDS)
            .writeTimeout(MAX_TIMEOUT_SECONDS_AI, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()
    }

    @Named("WebRetrofit")
    @SingleIn(AppScope::class)
    @Provides
    fun provideWebRetrofit(
        @Named("AIOkHttpClient") okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEB_SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Provides
    fun provideWebService(@Named("WebRetrofit") webRetrofit: Retrofit): WebService {
        return webRetrofit.create(WebService::class.java)
    }

    @Named("AIRetrofit")
    @SingleIn(AppScope::class)
    @Provides
    fun provideAIRetrofit(
        @Named("AIOkHttpClient") okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.AI_SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Provides
    fun provideAIService(@Named("AIRetrofit") aiRetrofit: Retrofit): AIService {
        return aiRetrofit.create(AIService::class.java)
    }

    @Named("OpenAIOkHttpClient")
    @SingleIn(AppScope::class)
    @Provides
    fun provideOpenAIOkHttpClient(
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

    @Named("OpenAIRetrofit")
    @SingleIn(AppScope::class)
    @Provides
    fun provideOpenAIRetrofit(
        @Named("OpenAIOkHttpClient") okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OPENAI_SERVER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Provides
    fun provideOpenAIService(@Named("OpenAIRetrofit") openAIRetrofit: Retrofit): OpenAIService {
        return openAIRetrofit.create(OpenAIService::class.java)
    }
}
