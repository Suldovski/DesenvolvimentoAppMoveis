package com.example.delivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
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
fun CarrinhoScreen(
    modifier: Modifier = Modifier,
    irParaConfirmacao: () -> Unit,
    adicionarMaisProdutos: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Meu Carrinho",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val subtotal = CarrinhoState.subtotal()

            if (CarrinhoState.itens.isEmpty()) {
                Text(
                    text = "Seu carrinho está vazio",
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                CarrinhoState.itens.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFFF3E0)
                                ) {}
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = item.nome, fontWeight = FontWeight.Bold)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            CarrinhoState.alterarQuantidade(item.nome, item.quantidade - 1)
                                        }) { Text("-", fontWeight = FontWeight.Bold) }
                                        Text("${item.quantidade}")
                                        IconButton(onClick = {
                                            CarrinhoState.alterarQuantidade(item.nome, item.quantidade + 1)
                                        }) { Text("+", fontWeight = FontWeight.Bold) }
                                    }
                                }
                            }
                            Text(
                                text = "R$ ${String.format(java.util.Locale("pt", "BR"), "%.2f", item.preco * item.quantidade).replace('.', ',')}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Subtotal", color = Color.Gray)
                Text(text = "R$ ${String.format(java.util.Locale("pt", "BR"), "%.2f", subtotal).replace('.', ',')}")
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Taxa de entrega", color = Color.Gray)
                Text(text = "R$ 5,00")
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = "R$ ${String.format(java.util.Locale("pt", "BR"), "%.2f", subtotal + 5.0).replace('.', ',')}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Text(
                text = "Entregar em",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            Text(text = "Rua das Flores, 123", fontSize = 14.sp)
            Text(text = "Centro - Curitiba, PR", color = Color.Gray, fontSize = 12.sp)
        }

        Button(
            onClick = adicionarMaisProdutos,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Adicionar mais produtos", color = Color(0xFF333333), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = irParaConfirmacao,
            enabled = CarrinhoState.itens.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 24.dp)
        ) {
            Text(text = "Confirmar Pedido", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
