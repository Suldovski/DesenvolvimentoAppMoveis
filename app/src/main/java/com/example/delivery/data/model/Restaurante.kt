package com.example.delivery.data.model

data class Restaurante(
    val id: Long,
    val nome: String,
    val avaliacao: String,
    val tempo: String,
    val cep: String = "",
    val rua: String = "",
    val bairro: String = "",
    val cidade: String = "",
    val uf: String = ""
)
