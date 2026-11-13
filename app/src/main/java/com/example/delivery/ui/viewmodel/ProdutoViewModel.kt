package com.example.delivery.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery.auth.SessaoUsuario
import com.example.delivery.data.dal.ProdutoDao
import com.example.delivery.data.model.Produto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProdutoViewModel(application: Application): AndroidViewModel(application) {
    private val dao = ProdutoDao(application)
    var produtos by mutableStateOf<List<Produto>>(emptyList())
        private set
    var produto by mutableStateOf<Produto?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var preco by mutableStateOf("")
        private set
    var categoriaId by mutableStateOf(0L)
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    private fun executar(op: suspend () -> Unit){
        if(carregando || salvo) return
        carregando = true
        mensagem = null
        viewModelScope.launch {
            try {
                SessaoUsuario.exigirUsuario()
                op()
            }catch(e: CancellationException){
                throw e
            }catch(e: IllegalArgumentException){
                mensagem = e.message
            }catch (e: Exception){
                mensagem = "Um erro inesperado aconteceu."
            }finally {
                carregando = false
            }
        }
    }

    fun listar() = executar {
        val lista = withContext(Dispatchers.IO) {dao.listar()}
        produtos = lista
    }

    fun buscarPorId(id: Long){
        if (produto?.id == id) return
        executar {
            val encontrado = withContext(Dispatchers.IO){ dao.buscarPorId(id) }
            produto = requireNotNull(encontrado) { "Produto não encontrado" }
            nome = encontrado.nome
            preco = encontrado.preco
            categoriaId = encontrado.categoriaId
        }
    }

    fun mudarNome(valor: String){
        if (!carregando && valor.length <= 100) nome = valor
    }

    fun mudarPreco(valor: String){
        if (!carregando && valor.length <= 20) preco = valor
    }

    fun mudarCategoriaId(valor: Long){
        if (!carregando) categoriaId = valor
    }

    fun cadastrar(nome: String, preco: String, categoriaId: Long) = executar {
        val nomeLimpo = nome.trim()
        val precoLimpo = preco.trim()
        require(nomeLimpo.length in 3..100) { "Nome inválido" }
        require(precoLimpo.isNotBlank()) { "Preço inválido" }
        require(categoriaId > 0) { "Categoria inválida" }
        withContext(Dispatchers.IO){
            dao.cadastrar(nomeLimpo, precoLimpo, categoriaId)
        }
        salvo = true
    }

    fun salvar() = executar {
        val selecionado = requireNotNull(produto) { "Selecione um produto" }
        require(nome.trim().length in 3..100) { "Nome inválido" }
        require(preco.trim().isNotBlank()) { "Preço inválido" }
        withContext(Dispatchers.IO){
            dao.alterar(selecionado.id, nome.trim(), preco.trim(), categoriaId)
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        withContext(Dispatchers.IO){ dao.excluir(id) }
        produtos = produtos.filterNot { it.id == id }
        mensagem = "Produto excluído"
    }

    fun consumirSalvo(){
        salvo = false
    }
}
