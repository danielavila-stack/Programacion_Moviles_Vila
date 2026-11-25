package com.vila.murocampus.data.remote

import com.vila.murocampus.model.Comentario
import com.vila.murocampus.model.NuevoPost
import com.vila.murocampus.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Describe las llamadas al servicio. Retrofit escribe el código por nosotros.
// Las rutas NO empiezan con "/" porque se pegan a la URL base.
interface PostApi {

    // GET posts → las 100 publicaciones
    @GET("posts")
    suspend fun listar(): List<Post>

    // GET posts/5 → una publicación ({id} se reemplaza con el número)
    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    // GET posts/5/comments → los comentarios de esa publicación
    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    // GET posts?userId=2 → solo las del usuario 2
    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    // POST posts → envía la publicación nueva en el cuerpo, como JSON
    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post
}