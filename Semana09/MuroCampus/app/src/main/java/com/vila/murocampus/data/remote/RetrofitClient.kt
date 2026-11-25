package com.vila.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    // Ignora campos que lleguen y no estén en las clases
    private val json = Json { ignoreUnknownKeys = true }

    // Mejora 1: agrega "Accept: application/json" a TODAS las peticiones
    private val encabezados = Interceptor { chain ->
        val peticion = chain.request().newBuilder()
            .header("Accept", "application/json")
            .build()
        chain.proceed(peticion)   // continúa con la petición ya modificada
    }

    // Muestra en Logcat cada petición y respuesta completas
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val cliente = OkHttpClient.Builder()
        .addInterceptor(encabezados)   // 1.º agrega el encabezado
        .addInterceptor(logging)       // 2.º registra la petición (ya con el encabezado)
        .build()

    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java)
    }
}