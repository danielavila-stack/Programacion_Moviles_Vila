package com.vila.murocampus.model

import kotlinx.serialization.Serializable

// Una publicación, tal como llega de GET posts y GET posts/{id}
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

// Un comentario, tal como llega de GET posts/{id}/comments
@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

// Lo que ENVIAMOS con POST posts: no lleva id, porque lo pone el servidor
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)