package com.example.delivery.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery.auth.SessaoUsuario
import com.example.delivery.data.dal.RestauranteDao
import com.example.delivery.data.model.Restaurante
import com.example.delivery.validation.RestauranteValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestauranteViewModel(application: Application): AndroidViewModel(application) {
    private val dao = RestauranteDao(application)
    var restaurantes by mutableStateOf<List<Restaurante>>(emptyList())
        private set
    var restaurante by mutableStateOf<Restaurante?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var avaliacao by mutableStateOf("")
        private set
    var tempo by mutableStateOf("")
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
        restaurantes = lista
    }

    fun buscarPorId(id: Long){
        if (restaurante?.id == id) return
        executar {
            val encontrado = withContext(Dispatchers.IO){ dao.buscarPorId(id) }
            restaurante = requireNotNull(encontrado) { "Restaurante não encontrado" }
            nome = encontrado.nome
            avaliacao = encontrado.avaliacao
            tempo = encontrado.tempo
        }
    }

    fun mudarNome(valor: String){
        if (!carregando && valor.length <= 100) nome = valor
    }

    fun mudarAvaliacao(valor: String){
        if (!carregando && valor.length <= 10) avaliacao = valor
    }

    fun mudarTempo(valor: String){
        if (!carregando && valor.length <= 20) tempo = valor
    }

    fun cadastrar(nome: String, avaliacao: String, tempo: String) = executar {
        val nomeLimpo = nome.trim()
        val avaliacaoLimpa = avaliacao.trim()
        val tempoLimpo = tempo.trim()

        RestauranteValidator.validar(nomeLimpo, avaliacaoLimpa, tempoLimpo)
        withContext(Dispatchers.IO){
            dao.cadastrar(nomeLimpo, avaliacaoLimpa, tempoLimpo)
        }
        salvo = true
    }

    fun salvar() = executar {
        val restauranteSelecionado = requireNotNull(restaurante) { "Selecione um restaurante" }

        RestauranteValidator.validar(nome, avaliacao, tempo)
        withContext(Dispatchers.IO){
            dao.alterar(restauranteSelecionado.id, nome, avaliacao, tempo)
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        withContext(Dispatchers.IO){ dao.excluir(id) }
        restaurantes = restaurantes.filterNot { it.id == id }
        mensagem = "Restaurante excluído"
    }

    fun consumirSalvo(){
        salvo = false
    }
}
