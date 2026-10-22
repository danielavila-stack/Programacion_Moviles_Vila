package com.tecsup.huellitasvet.data.local

import androidx.room.withTransaction
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.Sexo
import java.time.LocalDate
import java.time.LocalTime

// Datos de prueba ficticios (Ley 29733). Se cargan solo la primera vez.
object DatosIniciales {

    // ids fijos para que las claves foráneas coincidan
    private const val DANIELA = 1
    private const val LUIS = 2
    private const val VET = 3

    private const val CONSULTA = 1
    private const val VACUNACION = 2
    private const val BANO = 3
    private const val DESPARASITACION = 4

    private const val LUNA = 1
    private const val MAX = 2
    private const val ROCKY = 3
    private const val MICHI = 4

    suspend fun sembrar(db: VetDatabase) {
        if (db.usuarioDao().contar() > 0) return
        db.withTransaction {
            db.servicioDao().insertarTodos(servicios())
            db.usuarioDao().insertarTodos(usuarios())
            db.mascotaDao().insertarTodos(mascotas())
            db.citaDao().insertarTodos(citas())
        }
    }

    private fun servicios() = listOf(
        Servicio(id = CONSULTA, nombre = "Consulta general", descripcion = "Revisión completa de la mascota", duracionMin = 30, precio = 60.0, icono = "heart"),
        Servicio(id = VACUNACION, nombre = "Vacunación", descripcion = "Aplicación de vacunas según calendario", duracionMin = 30, precio = 45.0, icono = "syringe"),
        Servicio(id = BANO, nombre = "Baño y corte", descripcion = "Baño, secado y corte de pelo", duracionMin = 60, precio = 50.0, icono = "scissors"),
        Servicio(id = DESPARASITACION, nombre = "Desparasitación", descripcion = "Tratamiento antiparasitario interno y externo", duracionMin = 15, precio = 25.0, activo = false, icono = "heart")
    )

    private fun usuarios() = listOf(
        Usuario(id = DANIELA, nombre = "Daniela Fransua", correo = "daniela@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
        Usuario(id = LUIS, nombre = "Luis Ramos", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
        Usuario(id = VET, nombre = "Dra. Carla Rojas", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name)
    )

    private fun mascotas() = listOf(
        Mascota(id = LUNA, duenoId = DANIELA, nombre = "Luna", especie = Especie.GATO.name, raza = "Siamés", anioNacimiento = 2023, peso = 4.2, sexo = Sexo.HEMBRA.name),
        Mascota(id = MAX, duenoId = DANIELA, nombre = "Max", especie = Especie.PERRO.name, raza = "Labrador", anioNacimiento = 2021, peso = 24.5, sexo = Sexo.MACHO.name),
        Mascota(id = ROCKY, duenoId = LUIS, nombre = "Rocky", especie = Especie.PERRO.name, raza = "Bulldog", anioNacimiento = 2020, peso = 22.0, sexo = Sexo.MACHO.name),
        Mascota(id = MICHI, duenoId = LUIS, nombre = "Michi", especie = Especie.GATO.name, raza = "Mestizo", anioNacimiento = 2024, peso = 3.8, sexo = Sexo.HEMBRA.name)
    )

    // Fechas relativas a hoy: siempre hay citas pasadas, de hoy y futuras
    private fun dia(n: Long): String = LocalDate.now().plusDays(n).toString()

    // Primer horario libre de hoy a partir de dentro de 15 minutos (o null si ya cerró la clínica)
    private fun proximoHorarioDeHoy(): String? {
        val desde = LocalTime.now().plusMinutes(15)
        return Horarios.delDia().firstOrNull { !LocalTime.parse(it).isBefore(desde) }
    }

    private fun citas(): List<Cita> {
        val precio = servicios().associate { it.id to it.precio }
        fun cita(
            mascota: Int, servicio: Int, fecha: String, hora: String, estado: EstadoCita,
            diagnostico: String? = null, tratamiento: String? = null, peso: Double? = null, nota: String = ""
        ) = Cita(
            mascotaId = mascota, servicioId = servicio, fecha = fecha, hora = hora, nota = nota,
            estado = estado.name, precio = precio.getValue(servicio),
            diagnostico = diagnostico, tratamiento = tratamiento, pesoRegistrado = peso
        )

        // Cita confirmada que empieza pronto: sirve para probar que no se puede cancelar (< 2 horas)
        val pronto = proximoHorarioDeHoy()
        val citaPronto = if (pronto != null) cita(LUNA, VACUNACION, dia(0), pronto, EstadoCita.CONFIRMADA, nota = "Refuerzo anual")
        else cita(LUNA, VACUNACION, dia(1), "09:00", EstadoCita.CONFIRMADA, nota = "Refuerzo anual")
        val horaRockyHoy = if (pronto == "17:30") "17:00" else "17:30"

        return listOf(
            // Pasadas ATENDIDAS (historial clínico)
            cita(LUNA, BANO, dia(-200), "10:00", EstadoCita.ATENDIDA, "Higiene general, pelaje en buen estado", "Baño medicado", 4.0),
            cita(LUNA, CONSULTA, dia(-120), "11:00", EstadoCita.ATENDIDA, "Control de rutina. Sin hallazgos.", "Ninguno", 4.1),
            cita(LUNA, VACUNACION, dia(-30), "09:30", EstadoCita.ATENDIDA, "Vacuna triple felina aplicada", "Próximo refuerzo en 12 meses", 4.2),
            cita(MAX, CONSULTA, dia(-60), "16:00", EstadoCita.ATENDIDA, "Leve otitis en oído derecho", "Gotas óticas por 7 días", 24.5),
            // Atendida este mes (para los ingresos del mes)
            cita(MICHI, VACUNACION, LocalDate.now().withDayOfMonth(1).toString(), "10:00", EstadoCita.ATENDIDA, "Vacuna antirrábica aplicada", "Observación 24 horas", 3.8),
            // Cancelada
            cita(ROCKY, CONSULTA, dia(-10), "15:00", EstadoCita.CANCELADA),
            // Hoy
            citaPronto,
            cita(ROCKY, CONSULTA, dia(0), horaRockyHoy, EstadoCita.PENDIENTE, nota = "Revisión de piel"),
            // Próximos días
            cita(MICHI, CONSULTA, dia(1), "12:30", EstadoCita.PENDIENTE),
            cita(MAX, CONSULTA, dia(2), "10:00", EstadoCita.PENDIENTE, nota = "Control de oído"),
            cita(MAX, BANO, dia(3), "09:30", EstadoCita.PENDIENTE),
            cita(ROCKY, VACUNACION, dia(4), "11:00", EstadoCita.CONFIRMADA)
        )
    }
}
