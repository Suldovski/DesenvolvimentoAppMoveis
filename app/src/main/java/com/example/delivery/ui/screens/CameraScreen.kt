package com.example.delivery.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.delivery.ui.viewmodel.CameraViewModel

@Composable
fun CameraScreen(model: CameraViewModel = viewModel()) {
    val camera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { imagem -> model.receber(imagem) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Abra a câmera e tire uma foto")
        Button(
            enabled = !model.abrindo,
            onClick = {
                model.iniciar()
                try {
                    camera.launch(null)
                }catch (_: Exception){
                    model.informarFalha()
                }
            }

        ){
            Text("Tirar Foto")
        }

        model.mensagem?.let { Text(it) }

        model.foto?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Foto capturada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
