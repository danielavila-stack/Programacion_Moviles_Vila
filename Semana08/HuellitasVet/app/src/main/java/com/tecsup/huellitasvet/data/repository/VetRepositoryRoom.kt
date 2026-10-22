package com.tecsup.huellitasvet.data.repository

import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

// Implementación del contrato con Room. Solo esta clase conoce los DAOs.
class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    private val servicioDao = db.servicioDao()
    // TODO (Paso 4): usuarioDao, mascotaDao y citaDao

    // Servicios (ejemplo resuelto)
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) servicioDao.insertar(s) else servicioDao.actualizar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) = servicioDao.eliminar(s)

    override suspend fun cambiarActivo(id: Int, activo: Boolean) = servicioDao.cambiarActivo(id, activo)

    override suspend fun servicioTieneCitas(id: Int): Boolean =
        TODO("Paso 4: contar las citas del servicio con el CitaDao")

    // TODO (Paso 4): implementa el resto del contrato (usuarios, mascotas, citas y panel)
}
