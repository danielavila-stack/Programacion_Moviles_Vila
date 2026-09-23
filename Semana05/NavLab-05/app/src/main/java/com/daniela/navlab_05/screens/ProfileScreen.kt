package com.daniela.navlab_05.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.navlab_05.components.ProfileInfoGroup
import com.daniela.navlab_05.components.ProfileTopHeader
import com.daniela.navlab_05.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String,
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val email = "${userName.lowercase().replace(" ", ".")}@unmsm.edu.pe"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuración de Perfil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurplePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            ProfileTopHeader(
                name = userName,
                role = "Estudiante de Ingeniería de Software"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "INFORMACIÓN PERSONAL",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ProfileInfoGroup(
                icon = Icons.Default.Person,
                label = "Nombre Completo",
                value = userName
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileInfoGroup(
                icon = Icons.Default.Email,
                label = "Correo",
                value = email
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileInfoGroup(
                icon = Icons.Default.Phone,
                label = "Teléfono",
                value = "+51 987 654 321"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ACADÉMICO",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ProfileInfoGroup(
                icon = Icons.Default.School,
                label = "Carrera",
                value = "Ingeniería de Software"
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileInfoGroup(
                icon = Icons.Default.Book,
                label = "Ciclo Actual",
                value = "4to Ciclo - 2025-I"
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLogout,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
