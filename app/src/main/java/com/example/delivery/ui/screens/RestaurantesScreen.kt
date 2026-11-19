package com.example.delivery.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.viewmodel.RestauranteViewModel

@Composable
fun RestaurantesScreen(
    viewModel: RestauranteViewModel,
    irParaProduto: (Long, String) -> Unit,
    irParaCadastro: () -> Unit,
    irParaEdicao: (Long) -> Unit
) {
    var busca by remember { mutableStateOf("") }
    var excluirId by rememberSaveable { mutableStateOf<Long?>(null) }

    val coresTextField = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Olá! O que você\nquer comer?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    placeholder = { Text("Buscar restaurantes ou pratos") },
                    leadingIcon = { Text("🔍") },
                    colors = coresTextField,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFF9800),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Burger", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp)
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Sushi", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp)
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Lanches", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = irParaCadastro,
                    enabled = !viewModel.carregando,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("Novo restaurante", color = Color.White, fontWeight = FontWeight.Bold)
                }

                viewModel.mensagem?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
                }
                if (viewModel.carregando) CircularProgressIndicator()
                if (!viewModel.carregando && viewModel.restaurantes.isEmpty())
                    Text("Nenhum restaurante encontrado")
            }
        }

        items(viewModel.restaurantes, key = { it.id }) { rest ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { irParaProduto(rest.id, rest.nome) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 12.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFF9800)
                        ) {}

                        Column {
                            Text(text = rest.nome, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "★ ${rest.avaliacao} • ${rest.tempo}", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { irParaEdicao(rest.id) },
                            enabled = !viewModel.carregando
                        ) { Text("Alterar") }
                        TextButton(
                            onClick = { excluirId = rest.id },
                            enabled = !viewModel.carregando
                        ) { Text("Excluir") }
                    }
                }
            }
        }
    }

    val selecionado = viewModel.restaurantes.firstOrNull { it.id == excluirId }
    if (selecionado != null) {
        AlertDialog(
            onDismissRequest = { excluirId = null },
            title = { Text("Excluir restaurante?") },
            text = { Text("Excluir \"${selecionado.nome}\"? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        excluirId = null
                        viewModel.excluir(selecionado.id)
                    },
                    enabled = !viewModel.carregando
                ) { Text("Excluir") }
            },
            dismissButton = { TextButton(onClick = { excluirId = null }) { Text("Cancelar") } }
        )
    }
}
