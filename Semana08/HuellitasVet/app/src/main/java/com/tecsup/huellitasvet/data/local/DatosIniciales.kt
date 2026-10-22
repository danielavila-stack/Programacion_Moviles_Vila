package com.tecsup.huellitasvet.data.local

import com.tecsup.huellitasvet.data.local.entity.Servicio

// Datos de prueba ficticios (Ley 29733). Se cargan solo la primera vez.
object DatosIniciales {

    suspend fun sembrar(db: VetDatabase) {
        // TODO (Paso 3): cambia la condición a usuarioDao().contar() == 0 y siembra
        //  usuarios, mascotas y unas 10 citas (fechas con LocalDate.now().plusDays(n))
        if (db.servicioDao().contar() == 0) {
            db.servicioDao().insertarTodos(servicios())
        }
    }

    private fun servicios() = listOf(
        Servicio(nombre = "Consulta general", descripcion = "Revisión completa de la mascota", duracionMin = 30, precio = 60.0, icono = "heart"),
        Servicio(nombre = "Vacunación", descripcion = "Aplicación de vacunas según calendario", duracionMin = 30, precio = 45.0, icono = "syringe"),
        Servicio(nombre = "Baño y corte", descripcion = "Baño, secado y corte de pelo", duracionMin = 60, precio = 50.0, icono = "scissors"),
        Servicio(nombre = "Desparasitación", descripcion = "Tratamiento antiparasitario interno y externo", duracionMin = 15, precio = 25.0, activo = false, icono = "heart")
    )
}
