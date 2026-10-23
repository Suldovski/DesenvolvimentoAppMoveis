package com.example.delivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmadoScreen(
    modifier: Modifier = Modifier,
    irParaRestaurantes: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Surface(
                modifier = Modifier
                    .size(90.dp)
                    .padding(bottom = 16.dp),
                shape = CircleShape,
                color = Color(0xFFE8F5E9)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "✓", fontSize = 40.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Pedido confirmado!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Seu pedido está sendo\npreparado.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row {
                    Text(text = "Restaurante: ", color = Color.Gray)
                    Text(text = "Burger House", fontWeight = FontWeight.Bold)
                }
                Row {
                    Text(text = "Tempo estimado: ", color = Color.Gray)
                    Text(text = "30 min", fontWeight = FontWeight.Bold)
                }
                Row {
                    Text(text = "Número do pedido: ", color = Color.Gray)
                    Text(text = "#1025", fontWeight = FontWeight.Bold)
                }
            }
        }

        Button(
            onClick = irParaRestaurantes,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Voltar ao Início", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
