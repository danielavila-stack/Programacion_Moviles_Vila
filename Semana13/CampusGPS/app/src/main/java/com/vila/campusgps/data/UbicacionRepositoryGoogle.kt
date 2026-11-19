package com.vila.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.vila.campusgps.model.Posicion
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {

    private val appContext = context.applicationContext
    private val cliente = LocationServices.getFusedLocationProviderClient(appContext)

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        // getCurrentLocation devuelve null si no puede determinar la ubicación (GPS apagado)
        val location = cliente.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).await() ?: throw UbicacionNoDisponibleException()
        Posicion(location.latitude, location.longitude)
    }

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(appContext) == ConnectionResult.SUCCESS

    // MEJORA 3: convierte requestLocationUpdates (callbacks) en un Flow
    @SuppressLint("MissingPermission")
    override fun seguirPosicion(): Flow<Posicion> = callbackFlow {
        // Pide una ubicación cada 3 s, y solo si te moviste al menos 5 m
        val pedido = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000L)
            .setMinUpdateDistanceMeters(5f)
            .build()

        // Cada vez que Android tiene una ubicación nueva, la mandamos al Flow
        val callback = object : LocationCallback() {
            override fun onLocationResult(resultado: LocationResult) {
                resultado.lastLocation?.let {
                    trySend(Posicion(it.latitude, it.longitude))
                }
            }
        }

        cliente.requestLocationUpdates(pedido, callback, Looper.getMainLooper())
            .addOnFailureListener { close(it) } // si falla, el Flow termina con ese error

        // Cuando ya nadie escucha (Detener o se cierra el ViewModel): apagar el GPS
        awaitClose { cliente.removeLocationUpdates(callback) }
    }
}