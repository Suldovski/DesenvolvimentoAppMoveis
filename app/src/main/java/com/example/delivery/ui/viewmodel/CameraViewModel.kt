package com.example.delivery.ui.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CameraViewModel: ViewModel() {
    var foto by mutableStateOf<Bitmap?>(null)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var abrindo by mutableStateOf(false)
        private set

    fun iniciar(){
        foto = null
        mensagem = null
        abrindo = true
    }

    fun  receber(imagem: Bitmap?){
        foto = imagem
        abrindo = false
        mensagem = if(imagem == null) "Captura cancelada" else null
    }

    fun informarFalha(){
        abrindo = false
        mensagem = "Não foi possível abrir a camera"
    }
}
