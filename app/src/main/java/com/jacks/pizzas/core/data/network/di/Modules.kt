package com.jacks.pizzas.core.data.network.di

import coil.Coil
import coil.ImageLoader
import com.jacks.pizzas.pizzas.data.network.PizzaApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.time.Duration

private const val BASE_URL = "https://oursongapp.com"
private const val REQUEST_TIMEOUT_SECONDS = 60L

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    single {
        OkHttpClient.Builder()
            .readTimeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single<Converter.Factory> {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(get())
            .build()
    }

    single<PizzaApi> {
        get<Retrofit>().create(PizzaApi::class.java)
    }

    single<ImageLoader> {
        Coil.imageLoader(androidContext())
    }
}
