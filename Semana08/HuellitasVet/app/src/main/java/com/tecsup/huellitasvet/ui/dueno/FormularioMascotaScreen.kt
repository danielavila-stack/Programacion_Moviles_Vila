package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstiloBoton
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.OpcionesChip
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

// Sirve para crear (esNueva) y para editar
@Composable
fun FormularioMascotaScreen(
    vm: MascotaViewModel,
    esNueva: Boolean,
    onListo: () -> Unit
) {
    val form by vm.form.collectAsStateWithLifecycle()
    val errores by vm.errores.collectAsStateWithLifecycle()
    val terminado by vm.terminado.collectAsStateWithLifecycle()

    LaunchedEffect(terminado) { if (terminado) onListo() }

    Scaffold(
        topBar = { AppTopBar(if (esNueva) "Nueva mascota" else "Editar mascota", onBack = onListo) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Avatar según la especie elegida
            Box(Modifier.align(Alignment.CenterHorizontally)) {
                MascotaAvatar(form.especie ?: Especie.PERRO, tamano = 96.dp)
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .size(28.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(16.dp))

            VetTextField(form.nombre, vm::cambiarNombre, "Nombre", error = errores.nombre)
            Spacer(Modifier.height(10.dp))

            Etiqueta("Especie")
            OpcionesChip(Especie.entries, form.especie, { it.etiqueta }, vm::cambiarEspecie)
            ErrorCampo(errores.especie)
            Spacer(Modifier.height(10.dp))

            VetTextField(form.raza, vm::cambiarRaza, "Raza")
            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VetTextField(
                    form.anio, vm::cambiarAnio, "Año de nacimiento",
                    modifier = Modifier.weight(1f), error = errores.anio, teclado = KeyboardType.Number
                )
                VetTextField(
                    form.peso, vm::cambiarPeso, "Peso (kg)",
                    modifier = Modifier.weight(1f), error = errores.peso, teclado = KeyboardType.Decimal
                )
            }
            Spacer(Modifier.height(10.dp))

            Etiqueta("Sexo")
            OpcionesChip(Sexo.entries, form.sexo, { it.etiqueta }, vm::cambiarSexo, rellena = false)
            ErrorCampo(errores.sexo)
            Spacer(Modifier.height(24.dp))

            VetButton("Guardar mascota", onClick = vm::guardar)
            Spacer(Modifier.height(10.dp))
            VetButton("Cancelar", onClick = onListo, estilo = EstiloBoton.SECUNDARIO)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(texto, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
private fun ErrorCampo(error: String?) {
    if (error != null) {
        Text(
            error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp)
        )
    }
}
