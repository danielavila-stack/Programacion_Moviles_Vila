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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vila.centrotareas.model.PrimosUi
import com.vila.centrotareas.viewmodel.TareasViewModel

// Colores del recuadro de resultado (verde)
private val VerdeFondo = Color(0xFFE3F2E5)
private val VerdeTexto = Color(0xFF1B5E20)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()

    // Prueba de vida: si este número deja de subir, la pantalla está congelada
    var toques by rememberSaveable { mutableIntStateOf(0) }

    // Paso 5: texto del campo y último número calculado (solo interfaz)
    var textoNumero by rememberSaveable { mutableStateOf("500000") }
    var hastaCalculado by rememberSaveable { mutableIntStateOf(0) }
    val numero = textoNumero.toIntOrNull()                 // null si no es un número
    val numeroValido = numero != null && numero >= 2
    val calculando = primos is PrimosUi.Calculando

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ---------- Tarjeta 1: tarea con progreso ----------
            Tarjeta(titulo = "Tarea con progreso") {
                Button(onClick = { toques++ }) {
                    Text("Tócame: $toques")
                }

                // Paso 4: Iniciar deshabilitado mientras corre; Cancelar solo mientras corre
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { vm.tareaLarga() },
                        enabled = !corriendo
                    ) {
                        Text("Iniciar")
                    }
                    OutlinedButton(
                        onClick = { vm.cancelar() },
                        enabled = corriendo
                    ) {
                        Text("Cancelar")
                    }
                }

                // Paso 3: barra de progreso y porcentaje
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${(progreso * 100).toInt()} %")
                Text(mensaje)
            }

            // ---------- Tarjeta 2: contar números primos (Paso 5) ----------
            Tarjeta(titulo = "Contar números primos") {
                OutlinedTextField(
                    value = textoNumero,
                    onValueChange = { textoNumero = it },
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
                                hastaCalculado = numero
                                vm.contar(numero)
                            }
                        },
                        enabled = numeroValido && !calculando
                    ) {
                        Text("Contar")
                    }
                    OutlinedButton(
                        onClick = { vm.cancelarPrimos() },
                        enabled = calculando
                    ) {
                        Text("Cancelar")
                    }
                }

                // Resultado según el estado
                when (val p = primos) {
                    is PrimosUi.Inicial -> Text("Escribe un número y pulsa Contar")
                    is PrimosUi.Calculando -> {
                        CircularProgressIndicator()
                        Text("Calculando...")
                    }
                    is PrimosUi.Listo -> {
                        Surface(
                            color = VerdeFondo,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Primos hasta $hastaCalculado: ${p.cantidad}",
                                    color = VerdeTexto,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text("Tiempo: ${p.milisegundos} ms", color = VerdeTexto)
                            }
                        }
                    }
                    is PrimosUi.Cancelado -> Text("Cancelado")
                }
            }

            // ---------- Tarjeta 3: dos cargas ----------
            Tarjeta(titulo = "Dos cargas") {
                // TODO Paso 6: botones Una por una y A la vez, resultado
                // TODO Paso 7: interruptor Simular error y botón Reintentar
                Text("")
            }
        }
    }
}

// Tarjeta con el título en azul; se reutiliza para las tres tareas
@Composable
fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            contenido()
        }
    }
}