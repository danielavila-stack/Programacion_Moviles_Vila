package com.vila.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// Un solo cliente para toda la app (object = se crea una sola vez)
object RetrofitClient {

    // ignoreUnknownKeys: si el JSON trae campos que no están en el modelo, los ignora
    private val json = Json { ignoreUnknownKeys = true }

    // OkHttp hace la conexión; el interceptor muestra en Logcat lo que se envía y recibe
    private val cliente = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY   // solo para desarrollo
        })
        .build()

    // by lazy: se crea la primera vez que alguien usa "api", y nunca más
    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")   // termina con "/"
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java)
    }
}