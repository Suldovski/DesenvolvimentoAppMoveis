package com.example.delivery.ui.screens

import androidx.compose.runtime.mutableStateListOf

data class ItemCarrinho(
    val nome: String,
    val preco: Double,
    val quantidade: Int
)

object CarrinhoState {
    val itens = mutableStateListOf<ItemCarrinho>()

    fun adicionar(nome: String, preco: Double, quantidade: Int) {
        val existente = itens.find { it.nome == nome }
        if (existente != null) {
            val index = itens.indexOf(existente)
            itens[index] = existente.copy(quantidade = existente.quantidade + quantidade)
        } else {
            itens.add(ItemCarrinho(nome, preco, quantidade))
        }
    }

    fun alterarQuantidade(nome: String, quantidade: Int) {
        val item = itens.find { it.nome == nome } ?: return
        val index = itens.indexOf(item)
        if (quantidade <= 0) itens.removeAt(index)
        else itens[index] = item.copy(quantidade = quantidade)
    }

    fun limpar() = itens.clear()

    fun subtotal(): Double = itens.sumOf { it.preco * it.quantidade }
}
