package com.vila.campusgps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.vila.campusgps.R
import com.vila.campusgps.model.UbicacionUi
import com.vila.campusgps.ui.theme.VerdeCercano
import com.vila.campusgps.util.formatearDistancia
import com.vila.campusgps.viewmodel.MapaViewModel
import com.vila.campusgps.ui.components.FiltroCategorias
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(
    vm: MapaViewModel,
    modoOscuro: Boolean,
    onCambiarTema: () -> Unit
) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val lugaresVisibles by vm.lugaresVisibles.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val siguiendo by vm.siguiendo.collectAsState()
    val contexto = LocalContext.current

    // Recuerda qué quería hacer el usuario cuando se pidió el permiso
    var quiereSeguir by remember { mutableStateOf(false) }

    // Abre el cuadro de permiso de Android y recibe la respuesta del usuario
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            if (quiereSeguir) vm.iniciarSeguimiento() else vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun tienePermiso(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    fun pedirPermiso() {
        lanzador.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    fun alPulsar() {
        quiereSeguir = false
        if (tienePermiso()) vm.buscar() else pedirPermiso()
    }

    // MEJORA 3: iniciar / detener el seguimiento en vivo
    fun alPulsarSeguimiento() {
        if (siguiendo) {
            vm.detenerSeguimiento()
        } else {
            quiereSeguir = true
            if (tienePermiso()) vm.iniciarSeguimiento() else pedirPermiso()
        }
    }

    // Abre la pantalla de ajustes de esta app (para dar el permiso a mano)
    fun abrirAjustes() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", contexto.packageName, null)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        contexto.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // MEJORA 2: botón para cambiar entre modo claro y oscuro
                    IconButton(onClick = onCambiarTema) {
                        Icon(
                            painter = painterResource(R.drawable.ic_modo_oscuro),
                            contentDescription = if (modoOscuro) "Cambiar a modo claro" else "Cambiar a modo oscuro"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val e = estado) {

            // ---------- Pantalla de error ----------
            is UbicacionUi.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { alPulsar() }) {
                        Text("Obtener mi ubicación")
                    }
                    Spacer(Modifier.height(24.dp))
                    MensajeError(
                        error = e,
                        onAbrirAjustes = { abrirAjustes() },
                        onReintentar = { alPulsar() }
                    )
                }
            }

            // ---------- Pantalla normal (filtro + mapa + lista) ----------
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // MEJORA 1: chips de categoría arriba del mapa
                    FiltroCategorias(
                        categorias = vm.categorias,
                        seleccionada = categoria,
                        onSeleccionar = { vm.filtrar(it) }
                    )

                    // El mapa ocupa todo el espacio libre (solo lugares visibles)
                    MapaUbicacion(
                        estado = e,
                        lugares = lugaresVisibles,
                        seleccionado = seleccionado,
                        masCercanoId = masCercano?.id,
                        modoOscuro = modoOscuro,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    // Botones y estado
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { alPulsar() },
                                enabled = e !is UbicacionUi.Buscando && !siguiendo
                            ) {
                                Text("Obtener mi ubicación")
                            }
                            OutlinedButton(
                                onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                                enabled = masCercano != null
                            ) {
                                Text("Ir al más cercano")
                            }
                        }

                        // MEJORA 3: botón para iniciar / detener el seguimiento en vivo
                        FilledTonalButton(onClick = { alPulsarSeguimiento() }) {
                            Text(if (siguiendo) "Detener seguimiento" else "Iniciar seguimiento")
                        }
                        if (siguiendo) {
                            Text(
                                "Siguiendo tu ubicación en vivo",
                                style = MaterialTheme.typography.bodySmall,
                                color = VerdeCercano,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        masCercano?.let { cercano ->
                            val metros = lugaresVisibles.firstOrNull { it.lugar.id == cercano.id }?.metros
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

                        if (categoria != null) {
                            Text(
                                "Mostrando ${lugaresVisibles.size} de ${lugares.size} lugares",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        when (e) {
                            is UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
                            is UbicacionUi.Buscando -> CircularProgressIndicator()
                            is UbicacionUi.Lista -> {
                                Text(
                                    "Latitud: ${e.posicion.latitud}  ·  Longitud: ${e.posicion.longitud}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            is UbicacionUi.Error -> {} // se maneja arriba
                        }
                    }

                    // Lista (solo lugares visibles), con íconos y el más cercano en verde
                    ListaLugares(
                        lugares = lugaresVisibles,
                        seleccionado = seleccionado,
                        masCercanoId = masCercano?.id,
                        onClick = { id -> vm.seleccionar(id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
        }
    }
}