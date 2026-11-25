package com.vila.murocampus.data

import com.vila.murocampus.data.remote.PostApi
import com.vila.murocampus.model.Comentario
import com.vila.murocampus.model.NuevoPost
import com.vila.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

// El contrato: qué puede pedir el ViewModel. Todo devuelve Result (éxito o fallo).
interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun borrar(id: Int): Result<Int>   // Mejora 3: devuelve el código de respuesta
}

// La implementación real: usa la PostApi de Retrofit
class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    override suspend fun listar() = llamar { api.listar() }

    override suspend fun detalle(id: Int) = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int) = llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int) = llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost) = llamar { api.crear(nuevo) }

    // Mejora 3: con Response<Unit> Retrofit NO lanza error si el código es 404 o 500,
    // así que lo revisamos aquí. Si no fue exitoso, lanzamos HttpException y
    // llamar{} lo convierte en Result.failure. El ViewModel nunca ve el Response.
    override suspend fun borrar(id: Int) = llamar {
        val respuesta = api.borrar(id)
        if (respuesta.isSuccessful) respuesta.code()   // 200, 204…
        else throw HttpException(respuesta)
    }

    // Hace la llamada y convierte cualquier error en Result.failure.
    // Así no repetimos el try/catch en cada función.
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e                     // la cancelación nunca se traga
        } catch (e: Exception) {
            Result.failure(e)           // sin internet, error 404, JSON raro...
        }
}