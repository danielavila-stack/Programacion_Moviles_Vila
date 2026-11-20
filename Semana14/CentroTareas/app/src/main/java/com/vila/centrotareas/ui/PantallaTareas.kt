package com.vila.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vila.centrotareas.model.CargaUi
import com.vila.centrotareas.model.PrimosUi
import com.vila.centrotareas.ui.components.BarraNavegacion
import com.vila.centrotareas.ui.components.CirculoProgreso
import com.vila.centrotareas.ui.components.RecuadroVerde
import com.vila.centrotareas.ui.components.Tarjeta
import com.vila.centrotareas.ui.theme.VerdeFondo
import com.vila.centrotareas.ui.theme.VerdeTexto
import com.vila.centrotareas.viewmodel.TareasViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    // MEJORA 3: pestaña elegida (estado de interfaz; se mantiene al rotar)
    var pestana by rememberSaveable { mutableIntStateOf(0) }

    // Estados de interfaz que deben sobrevivir al cambiar de pestaña:
    // se guardan aquí y no dentro de cada tarjeta
    var toques by rememberSaveable { mutableIntStateOf(0) }
    var textoNumero by rememberSaveable { mutableStateOf("500000") }
    var hastaCalculado by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            // MEJORA 3: barra de navegación inferior (componente en ui/components)
            BarraNavegacion(
                seleccionada = pestana,
                onSeleccionar = { pestana = it }
            )
        }
    ) { padding ->
        // Cargas ocupa toda la pantalla (sin scroll); Tarea y Primos pueden hacer scroll
        val desplazable = if (pestana == 2) Modifier else Modifier.verticalScroll(rememberScrollState())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .then(desplazable)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // MEJORA 3: solo se muestra la tarjeta de la pestaña elegida
            when (pestana) {
                0 -> TarjetaTarea(
                    vm = vm,
                    toques = toques,
                    onTocar = { toques++ }
                )
                1 -> TarjetaPrimos(
                    vm = vm,
                    textoNumero = textoNumero,
                    onTextoCambia = { textoNumero = it },
                    hastaCalculado = hastaCalculado,
                    onContar = { hastaCalculado = it }
                )
                2 -> TarjetaCargas(
                    vm = vm,
                    modifier = Modifier.weight(1f)   // la tarjeta llena todo el espacio
                )
            }
        }
    }
}

// ---------- Tarjeta 1: tarea con progreso (pasos 1 a 4) ----------
@Composable
fun TarjetaTarea(vm: TareasViewModel, toques: Int, onTocar: () -> Unit) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()

    Tarjeta(titulo = "Tarea con progreso") {
        // Prueba de vida: si este número deja de subir, la pantalla está congelada
        Button(onClick = onTocar) {
            Text("Tócame: $toques")
        }

        // Paso 4: Iniciar deshabilitado mientras corre; Cancelar solo mientras corre
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.tareaLarga() }, enabled = !corriendo) {
                Text("Iniciar")
            }
            OutlinedButton(onClick = { vm.cancelar() }, enabled = corriendo) {
                Text("Cancelar")
            }
        }

        // Paso 3: progreso en un círculo, con el porcentaje y el mensaje al centro
        CirculoProgreso(
            progreso = progreso,
            texto = mensaje,
            tamano = 220.dp
        )
    }
}

// ---------- Tarjeta 2: contar números primos (paso 5 + MEJORA 1) ----------
@Composable
fun TarjetaPrimos(
    vm: TareasViewModel,
    textoNumero: String,
    onTextoCambia: (String) -> Unit,
    hastaCalculado: Int,
    onContar: (Int) -> Unit
) {
    val primos by vm.primos.collectAsState()
    val progresoPrimos by vm.progresoPrimos.collectAsState()

    val numero = textoNumero.toIntOrNull()                 // null si no es un número
    val numeroValido = numero != null && numero >= 2
    val calculando = primos is PrimosUi.Calculando

    Tarjeta(titulo = "Contar números primos") {
        OutlinedTextField(
            value = textoNumero,
            onValueChange = onTextoCambia,
            label = { Text("Hasta el número") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = textoNumero.isNotEmpty() && !numeroValido,
            supportingText = {
                if (textoNumero.isNotEmpty() && !numeroValido) {
                    Text("Escribe un número entero mayor o igual a 2")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (numero != null) {
                        onContar(numero)
                        vm.contar(numero)
                    }
                },
                enabled = numeroValido && !calculando
            ) {
                Text("Contar")
            }
            OutlinedButton(onClick = { vm.cancelarPrimos() }, enabled = calculando) {
                Text("Cancelar")
            }
        }

        when (val p = primos) {
            is PrimosUi.Inicial -> Text("Escribe un número y pulsa Contar")
            is PrimosUi.Calculando -> {
                // MEJORA 1: círculo más grande con el porcentaje del cálculo
                CirculoProgreso(
                    progreso = progresoPrimos,
                    texto = "Calculando",
                    tamano = 240.dp
                )
            }
            is PrimosUi.Listo -> {
                RecuadroVerde {
                    Text(
                        "Primos hasta $hastaCalculado: ${p.cantidad}",
                        color = VerdeTexto,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text("Tiempo: ${p.milisegundos} ms", color = VerdeTexto)
                }
            }
            is PrimosUi.Cancelado ->
                Text("Cancelado al ${(progresoPrimos * 100).toInt()} %")
        }
    }
}

// ---------- Tarjeta 3: dos cargas (pasos 6 y 7 + MEJORA 2) ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaCargas(vm: TareasViewModel, modifier: Modifier = Modifier) {
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val limiteMs by vm.limiteMs.collectAsState()
    val cargando = carga is CargaUi.Cargando

    Tarjeta(titulo = "Dos cargas", modifier = modifier, llenarAlto = true) {
        // Paso 7: interruptor Simular error
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Switch(
                checked = simularError,
                onCheckedChange = { vm.cambiarSimularError(it) },
                enabled = !cargando
            )
            Text("Simular error")
        }

        // MEJORA 2: selector de tiempo máximo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Tiempo máximo:", style = MaterialTheme.typography.bodyMedium)
            FilterChip(
                selected = limiteMs == null,
                onClick = { vm.cambiarLimite(null) },
                label = { Text("Sin límite") },
                enabled = !cargando
            )
            FilterChip(
                selected = limiteMs == 2000L,
                onClick = { vm.cambiarLimite(2000L) },
                label = { Text("2 s") },
                enabled = !cargando
            )
            FilterChip(
                selected = limiteMs == 4000L,
                onClick = { vm.cambiarLimite(4000L) },
                label = { Text("4 s") },
                enabled = !cargando
            )
        }

        // Botones más altos, mitad y mitad
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { vm.cargarUnaPorUna() },
                enabled = !cargando,
                modifier = Modifier.weight(1f).height(56.dp)
            ) {
                Text("Una por una", fontSize = 16.sp)
            }
            Button(
                onClick = { vm.cargarALaVez() },
                enabled = !cargando,
                modifier = Modifier.weight(1f).height(56.dp)
            ) {
                Text("A la vez", fontSize = 16.sp)
            }
        }

        // El área del resultado ocupa todo el espacio que sobra
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val c = carga) {
                is CargaUi.Inicial -> Text(
                    "Elige cómo cargar los datos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                is CargaUi.Cargando -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            strokeWidth = 6.dp
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("Cargando...")
                    }
                }
                is CargaUi.Exito -> {
                    Surface(
                        color = VerdeFondo,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Modo: ${c.modo}", color = VerdeTexto, fontWeight = FontWeight.Medium)
                            Text(
                                "${c.milisegundos} ms",
                                color = VerdeTexto,
                                fontWeight = FontWeight.Bold,
                                fontSize = 52.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text("Perfil: ${c.perfil}", color = VerdeTexto, fontSize = 16.sp)
                            Text("Pedidos: ${c.pedidos}", color = VerdeTexto, fontSize = 16.sp)
                        }
                    }
                }
                is CargaUi.Error -> {
                    // Paso 7: recuadro de error y botón Reintentar
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                c.mensaje,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        OutlinedButton(onClick = { vm.reintentar() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}