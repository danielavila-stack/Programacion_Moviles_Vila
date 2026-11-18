package com.vila.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.vila.campusgps.model.LugarConDistancia
import com.vila.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Cuando llega la ubicación, la cámara viaja hasta ella
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val p = estado.posicion
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(p.latitud, p.longitud), 16f)
            )
        }
    }

    // Cuando se selecciona un lugar, la cámara viaja hasta él
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
            ?: return@LaunchedEffect
        camara.animate(
            CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), 17f)
        )
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // Marcador propio (rojo por defecto), solo si ya tenemos ubicación
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Estás aquí"
            )
        }

        // Un marcador por cada lugar: verde el más cercano, azul los demás
        lugares.forEach { item ->
            key(item.lugar.id) {
                val estadoMarcador = rememberUpdatedMarkerState(
                    position = LatLng(item.lugar.latitud, item.lugar.longitud)
                )
                LaunchedEffect(seleccionado) {
                    if (item.lugar.id == seleccionado) estadoMarcador.showInfoWindow()
                }
                val color = if (item.lugar.id == masCercanoId)
                    BitmapDescriptorFactory.HUE_GREEN
                else
                    BitmapDescriptorFactory.HUE_AZURE

                Marker(
                    state = estadoMarcador,
                    title = item.lugar.nombre,
                    snippet = item.lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(color)
                )
            }
        }
    }
}