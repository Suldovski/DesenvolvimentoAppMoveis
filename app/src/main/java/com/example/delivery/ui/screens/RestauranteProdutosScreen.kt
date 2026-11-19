package com.example.delivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.viewmodel.ProdutoViewModel

@Composable
fun RestauranteProdutosScreen(
    viewModel: ProdutoViewModel,
    nomeRestaurante: String,
    aoAdicionar: (String, Double) -> Unit,
    voltar: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.listar() }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(nomeRestaurante, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Produtos disponíveis", color = androidx.compose.ui.graphics.Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))
        }

        items(viewModel.produtos, key = { it.id }) { produto ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(produto.nome, fontWeight = FontWeight.Bold)
                        Text("R$ ${produto.preco}")
                    }
                    Button(onClick = {
                        val preco = produto.preco.replace(',', '.').toDoubleOrNull() ?: 0.0
                        aoAdicionar(produto.nome, preco)
                    }) { Text("Adicionar") }
                }
            }
        }

        item {
            Button(onClick = voltar, modifier = Modifier.fillMaxWidth()) {
                Text("Ver carrinho")
            }
        }
    }
}
