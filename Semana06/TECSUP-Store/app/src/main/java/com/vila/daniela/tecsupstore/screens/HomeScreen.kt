package com.vila.daniela.tecsupstore.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.vila.daniela.tecsupstore.components.TarjetaProducto
import com.vila.daniela.tecsupstore.model.Producto

@Composable
fun HomeScreen(
    productos: List<Producto>
) {
    LazyColumn {
        items(productos) { producto ->
            TarjetaProducto(producto = producto)
        }
    }
}