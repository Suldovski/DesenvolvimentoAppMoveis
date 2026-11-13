package com.example.delivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Alterar restaurante",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        viewModel.mensagem?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
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

            Text("Endereço", fontWeight = FontWeight.SemiBold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.cep,
                    onValueChange = viewModel::mudarCep,
                    label = { Text("CEP") },
                    singleLine = true,
                    enabled = !viewModel.carregando && !viewModel.consultandoCep,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = viewModel::consultarCep,
                    enabled = !viewModel.carregando && !viewModel.consultandoCep,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Buscar", color = Color.White)
                }
            }

            if (viewModel.consultandoCep) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            OutlinedTextField(
                value = viewModel.rua,
                onValueChange = viewModel::mudarRua,
                label = { Text("Rua") },
                singleLine = true,
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.bairro,
                onValueChange = viewModel::mudarBairro,
                label = { Text("Bairro") },
                singleLine = true,
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.cidade,
                    onValueChange = viewModel::mudarCidade,
                    label = { Text("Cidade") },
                    singleLine = true,
                    enabled = !viewModel.carregando,
                    modifier = Modifier.weight(2f)
                )
                OutlinedTextField(
                    value = viewModel.uf,
                    onValueChange = viewModel::mudarUf,
                    label = { Text("UF") },
                    singleLine = true,
                    enabled = !viewModel.carregando,
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = { viewModel.salvar() },
                enabled = !viewModel.carregando && !viewModel.salvo && !viewModel.consultandoCep,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar alterações", color = Color.White, fontWeight = FontWeight.Bold)
            }
        } else if (!viewModel.carregando) {
            TextButton(onClick = { viewModel.buscarPorId(restauranteId) }) {
                Text("Tentar carregar novamente")
            }
        }

        TextButton(onClick = voltar, enabled = !viewModel.carregando) {
            Text("Voltar")
        }
    }
}
