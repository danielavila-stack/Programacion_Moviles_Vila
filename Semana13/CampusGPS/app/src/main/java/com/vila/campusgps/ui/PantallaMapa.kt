package com.vila.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.vila.campusgps.model.UbicacionUi
import com.vila.campusgps.util.formatearDistancia
import com.vila.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val contexto = LocalContext.current

    // Abre el cuadro de permiso de Android y recibe la respuesta del usuario
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        // Si concedió FINE o COARSE, busca; si no, muestra el error
        if (resultado.values.any { it }) vm.buscar() else vm.permisoDenegado()
    }

    fun alPulsar() {
        val tieneFine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val tieneCoarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (tieneFine || tieneCoarse) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. El mapa ocupa todo el espacio libre de arriba
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.id,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // 2. Botones y estado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { alPulsar() },
                    enabled = estado !is UbicacionUi.Buscando
                ) {
                    Text("Obtener mi ubicación")
                }

                // Botón secundario: deshabilitado hasta que haya un más cercano
                OutlinedButton(
                    onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                    enabled = masCercano != null
                ) {
                    Text("Ir al más cercano")
                }

                // Texto "Más cercano: nombre (distancia)"
                masCercano?.let { cercano ->
                    val metros = lugares.firstOrNull { it.lugar.id == cercano.id }?.metros
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Más cercano: ")
                            }
                            append(cercano.nombre)
                            if (metros != null) append(" (${formatearDistancia(metros)})")
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(4.dp))
                when (val e = estado) {
                    is UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
                    is UbicacionUi.Buscando -> CircularProgressIndicator()
                    is UbicacionUi.Lista -> {
                        Text(
                            "Latitud: ${e.posicion.latitud}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "Longitud: ${e.posicion.longitud}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    is UbicacionUi.Error -> Text(e.mensaje, color = MaterialTheme.colorScheme.error)
                }
            }

            // 3. Lista de lugares con altura fija debajo del mapa
            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = { id -> vm.seleccionar(id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}