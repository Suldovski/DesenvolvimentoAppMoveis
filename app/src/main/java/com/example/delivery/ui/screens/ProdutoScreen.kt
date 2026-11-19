package com.example.delivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.data.model.Produto

@Composable
fun ProdutoScreen(
    modifier: Modifier = Modifier,
    produto: Produto,
    irParaCarrinho: () -> Unit,
    aoAdicionar: (nome: String, preco: Double, quantidade: Int) -> Unit
) {
    var quantidade by remember { mutableIntStateOf(1) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Burger House",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF3E0)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "🍔", fontSize = 64.sp)
                }
            }

            Text(
                text = produto.nome,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Produto selecionado",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "R$ ${produto.preco.replace('.', ',')}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Quantidade",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { if (quantidade > 1) quantidade-- }) {
                    Text(text = "-", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Text(text = "$quantidade", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                IconButton(onClick = { quantidade++ }) {
                    Text(text = "+", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }

        Button(
            onClick = {
                val preco = produto.preco.replace(',', '.').toDoubleOrNull() ?: 0.0
                aoAdicionar(produto.nome, preco, quantidade)
                irParaCarrinho()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Adicionar ao Carrinho", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
