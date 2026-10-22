package com.tecsup.huellitasvet.model

// Perfil del usuario. Se guarda en la tabla como Rol.name
enum class Rol(val etiqueta: String) {
    CLIENTE("Dueño"),
    ADMIN("Veterinario")
}
