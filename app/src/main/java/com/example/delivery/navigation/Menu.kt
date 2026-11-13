package com.example.delivery.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.delivery.auth.SessaoUsuario

@Composable
fun Menu(navController: NavHostController) {
    BottomAppBar(
        containerColor = Color(0xFFFFF3E0)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.navigate(Restaurantes) }) {
                Text("Rest.", fontSize = 12.sp)
            }
            TextButton(onClick = { navController.navigate(Categorias) }) {
                Text("Cat.", fontSize = 12.sp)
            }
            TextButton(onClick = { navController.navigate(Produtos) }) {
                Text("Prod.", fontSize = 12.sp)
            }
            TextButton(onClick = { navController.navigate(Camera) }) {
                Text("Cam.", fontSize = 12.sp)
            }
            TextButton(onClick = { navController.navigate(Usuarios) }) {
                Text("User", fontSize = 12.sp)
            }
            TextButton(
                onClick = {
                    SessaoUsuario.encerrar()
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) {
                Text("Sair", fontSize = 12.sp)
            }
        }
    }
}
