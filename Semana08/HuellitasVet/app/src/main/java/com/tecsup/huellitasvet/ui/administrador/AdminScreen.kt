package com.tecsup.huellitasvet.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstiloBoton
import com.tecsup.huellitasvet.ui.components.TarjetaUsuario
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.AdminViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun AdminScreen(
    vm: AdminViewModel,
    sesion: SesionViewModel,
    onCerrarSesion: () -> Unit
) {
    val usuario by sesion.usuarioActual.collectAsStateWithLifecycle()
    val usuarios by vm.usuarios.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar("Administración") }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                val nombreAdmin = listOfNotNull(usuario?.nombre, usuario?.apellido)
                    .filter { it.isNotBlank() }
                    .joinToString(" ")
                TarjetaUsuario(
                    nombre = nombreAdmin,
                    correo = usuario?.correo.orEmpty(),
                    etiqueta = "Código: ${usuario?.codigo.orEmpty()}"
                )
            }
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Cuentas registradas (${usuarios.size})",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(usuarios) { u ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            val nombreMostrar = listOfNotNull(u.nombre, u.apellido)
                                .filter { it.isNotBlank() }
                                .joinToString(" ")
                            Text(nombreMostrar, style = MaterialTheme.typography.titleSmall)
                            Text(
                                u.correo,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        val rolEtiqueta = runCatching { Rol.valueOf(u.rol).etiqueta }.getOrDefault(u.rol)
                        Text(
                            rolEtiqueta,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(12.dp))
                VetButton(
                    texto = "Cerrar sesión",
                    onClick = onCerrarSesion,
                    estilo = EstiloBoton.PELIGRO
                )
            }
        }
    }
}
