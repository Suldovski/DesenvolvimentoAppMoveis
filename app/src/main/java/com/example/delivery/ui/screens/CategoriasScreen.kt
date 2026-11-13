package com.example.delivery.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.delivery.ui.viewmodel.CategoriaViewModel

@Composable
fun CategoriasScreen(
    viewModel: CategoriaViewModel,
    irParaCadastro: () -> Unit,
    irParaEdicao: (Long) -> Unit
) {
    var excluirId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.listar()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Categorias",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(onClick = irParaCadastro, modifier = Modifier.fillMaxWidth()) {
                Text("Nova Categoria")
            }
        }

        if (viewModel.carregando) {
            item { CircularProgressIndicator() }
        }

        items(viewModel.categorias) { cat ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { irParaEdicao(cat.id) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(cat.nome)
                    TextButton(onClick = { excluirId = cat.id }) {
                        Text("Excluir")
                    }
                }
            }
        }
    }

    excluirId?.let { id ->
        AlertDialog(
            onDismissRequest = { excluirId = null },
            title = { Text("Excluir?") },
            text = { Text("Confirma a exclusão da categoria?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.excluir(id)
                    excluirId = null
                }) { Text("Sim") }
            },
            dismissButton = {
                TextButton(onClick = { excluirId = null }) { Text("Não") }
            }
        )
    }
}
