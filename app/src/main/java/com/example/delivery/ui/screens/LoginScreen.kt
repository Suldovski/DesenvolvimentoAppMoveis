package com.example.delivery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    loginSucesso: () -> Unit,
    irCadastro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.concluido) {
        if (viewModel.concluido) {
            viewModel.consumirResultado()
            loginSucesso()
        }
    }

    val coresTextField = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
        disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            shape = CircleShape,
            color = Color(0xFFFF9800)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "F", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Text(
            text = "FastFood",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Text(text = "E-mail", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { if (it.length <= 254) email = it },
                placeholder = { Text("nome@email.com") },
                colors = coresTextField,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text(text = "Senha", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
            OutlinedTextField(
                value = senha,
                onValueChange = { if (it.length <= 128) senha = it },
                placeholder = { Text("••••••••") },
                colors = coresTextField,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (senhaVisivel) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagem = if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    val descricao = if (senhaVisivel) "Esconder Senha" else "Mostrar Senha"
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(imageVector = imagem, contentDescription = descricao)
                    }
                },
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
        }

        viewModel.mensagem?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        }
        if (viewModel.carregando) Text("Aguarde...", modifier = Modifier.padding(bottom = 8.dp))

        Button(
            onClick = {
                viewModel.login(email, senha)
                senha = ""
            },
            enabled = !viewModel.carregando,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(text = "Entrar", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "Ainda não possui uma conta?",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        TextButton(onClick = irCadastro, enabled = !viewModel.carregando) {
            Text("Cadastre-se", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}
