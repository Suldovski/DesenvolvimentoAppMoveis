package com.example.delivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.delivery.ui.viewmodel.RestauranteViewModel

@Composable
fun RestauranteEditarScreen(
    restauranteId: Long,
    viewModel: RestauranteViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(restauranteId) { viewModel.buscarPorId(restauranteId) }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Alterar restaurante", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        viewModel.mensagem?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (viewModel.carregando) CircularProgressIndicator()

        val restaurante = viewModel.restaurante
        if (restaurante != null) {
            OutlinedTextField(
                value = viewModel.nome,
                onValueChange = viewModel::mudarNome,
                label = { Text("Nome") },
                singleLine = true,
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.avaliacao,
                onValueChange = viewModel::mudarAvaliacao,
                label = { Text("Avaliação (0 a 5)") },
                singleLine = true,
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.tempo,
                onValueChange = viewModel::mudarTempo,
                label = { Text("Tempo de entrega") },
                singleLine = true,
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.salvar() },
                enabled = !viewModel.carregando && !viewModel.salvo,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Salvar alterações", color = Color.White, fontWeight = FontWeight.Bold) }
        } else if (!viewModel.carregando) {
            TextButton(onClick = { viewModel.buscarPorId(restauranteId) }) { Text("Tentar carregar novamente") }
        }
        TextButton(onClick = voltar, enabled = !viewModel.carregando) { Text("Voltar") }
    }
}
