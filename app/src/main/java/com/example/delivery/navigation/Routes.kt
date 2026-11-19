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
data class RestauranteProdutos(val id: Long, val nome: String)

@Serializable
data class Produto(val id: Long)

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

@Serializable
data object Categorias

@Serializable
data object CategoriaCadastrar

@Serializable
data class CategoriaEditar(val id: Long)

@Serializable
data object Produtos

@Serializable
data object ProdutoCadastrar

@Serializable
data class ProdutoEditar(val id: Long)

@Serializable
data object Camera
