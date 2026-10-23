package com.example.delivery.validation

object RestauranteValidator {
    fun validar(nome: String, avaliacao: String, tempo: String){
        require(nome.length in 3 .. 100){
            "Informe um nome entre 3 e 100 caracteres."
        }

        val valorAvaliacao = avaliacao.toDoubleOrNull()
        require(valorAvaliacao != null && valorAvaliacao in 0.0..5.0){
            "Informe uma avaliação entre 0 e 5."
        }

        require(tempo.length in 1 .. 20){
            "Informe um tempo entre 1 e 20 caracteres."
        }
    }
}
