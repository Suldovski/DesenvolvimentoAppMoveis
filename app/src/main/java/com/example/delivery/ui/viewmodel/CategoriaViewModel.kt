package com.example.delivery.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery.auth.SessaoUsuario
import com.example.delivery.data.dal.CategoriaDao
import com.example.delivery.data.model.Categoria
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriaViewModel(application: Application): AndroidViewModel(application) {
    private val dao = CategoriaDao(application)
    var categorias by mutableStateOf<List<Categoria>>(emptyList())
        private set
    var categoria by mutableStateOf<Categoria?>(null)
        private set
    var nome by mutableStateOf("")
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
        categorias = lista
    }

    fun buscarPorId(id: Long){
        if (categoria?.id == id) return
        executar {
            val encontrado = withContext(Dispatchers.IO){ dao.buscarPorId(id) }
            categoria = requireNotNull(encontrado) { "Categoria não encontrada" }
            nome = encontrado.nome
        }
    }

    fun mudarNome(valor: String){
        if (!carregando && valor.length <= 50) nome = valor
    }

    fun cadastrar(nome: String) = executar {
        val nomeLimpo = nome.trim()
        require(nomeLimpo.length in 2..50) { "Nome inválido" }
        withContext(Dispatchers.IO){
            dao.cadastrar(nomeLimpo)
        }
        salvo = true
    }

    fun salvar() = executar {
        val selecionada = requireNotNull(categoria) { "Selecione uma categoria" }
        require(nome.trim().length in 2..50) { "Nome inválido" }
        withContext(Dispatchers.IO){
            dao.alterar(selecionada.id, nome.trim())
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        withContext(Dispatchers.IO){ dao.excluir(id) }
        categorias = categorias.filterNot { it.id == id }
        mensagem = "Categoria excluída"
    }

    fun consumirSalvo(){
        salvo = false
    }
}
