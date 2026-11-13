package com.example.delivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.delivery.ui.viewmodel.ProdutoViewModel

@Composable
fun ProdutoCadastroScreen(
    viewModel: ProdutoViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Novo Produto")
        OutlinedTextField(
            value = viewModel.nome,
            onValueChange = viewModel::mudarNome,
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.carregando
        )
        OutlinedTextField(
            value = viewModel.preco,
            onValueChange = viewModel::mudarPreco,
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.carregando
        )
        OutlinedTextField(
            value = viewModel.categoriaId.toString(),
            onValueChange = { viewModel.mudarCategoriaId(it.toLongOrNull() ?: 0L) },
            label = { Text("ID Categoria") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.carregando
        )
        viewModel.mensagem?.let { Text(it) }
        Button(
            onClick = { viewModel.cadastrar(viewModel.nome, viewModel.preco, viewModel.categoriaId) },
            enabled = !viewModel.carregando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }
        Button(onClick = voltar, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}
