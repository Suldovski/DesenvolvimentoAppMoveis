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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.delivery.ui.viewmodel.RestauranteViewModel

@Composable
fun RestauranteCadastroScreen(
    viewModel: RestauranteViewModel,
    voltar: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var avaliacao by remember { mutableStateOf("") }
    var tempo by remember { mutableStateOf("") }

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
        Text("Novo restaurante", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = nome,
            onValueChange = { if (it.length <= 100) nome = it },
            label = { Text("Nome") },
            singleLine = true,
            enabled = !viewModel.carregando,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = avaliacao,
            onValueChange = { if (it.length <= 10) avaliacao = it },
            label = { Text("Avaliação (0 a 5)") },
            singleLine = true,
            enabled = !viewModel.carregando,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tempo,
            onValueChange = { if (it.length <= 20) tempo = it },
            label = { Text("Tempo de entrega") },
            singleLine = true,
            enabled = !viewModel.carregando,
            modifier = Modifier.fillMaxWidth()
        )

        viewModel.mensagem?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (viewModel.carregando) Text("Aguarde...")

        Button(
            onClick = { viewModel.cadastrar(nome, avaliacao, tempo) },
            enabled = !viewModel.carregando,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cadastrar", color = Color.White, fontWeight = FontWeight.Bold) }

        TextButton(onClick = voltar, enabled = !viewModel.carregando) { Text("Voltar") }
    }
}
