package com.example.delivery.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data object Cadastro

@Serializable
data object Restaurantes

@Serializable
data object RestauranteCadastrar

@Serializable
data class RestauranteEditar(val id: Long)

@Serializable
data object Produto

@Serializable
data object Carrinho

@Serializable
data object Confirmado

@Serializable
data object Usuarios

@Serializable
data object CadastrarUsuario

@Serializable
data class UsuarioEditar(val id: Long)
