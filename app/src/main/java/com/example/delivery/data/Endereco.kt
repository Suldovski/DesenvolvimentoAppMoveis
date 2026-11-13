package com.example.delivery.data

import com.google.gson.annotations.SerializedName

data class Endereco(
    val cep: String? = null,
    @SerializedName("logradouro")
    val rua: String? = null,
    val complemento: String? = null,
    val bairro: String? = null,
    @SerializedName("localidade")
    val cidade: String? = null,
    val uf: String? = null,
    val ddd: String? = null,
    val erro: Boolean? = null
)
