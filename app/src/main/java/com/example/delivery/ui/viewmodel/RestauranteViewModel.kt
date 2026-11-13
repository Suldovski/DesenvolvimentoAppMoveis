package com.example.delivery.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery.auth.SessaoUsuario
import com.example.delivery.data.ViaCepApi
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
    var cep by mutableStateOf("")
        private set
    var rua by mutableStateOf("")
        private set
    var bairro by mutableStateOf("")
        private set
    var cidade by mutableStateOf("")
        private set
    var uf by mutableStateOf("")
        private set

    var carregando by mutableStateOf(false)
        private set
    var consultandoCep by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    private fun executar(op: suspend () -> Unit){
        if (carregando || salvo) return
        carregando = true
        mensagem = null
        viewModelScope.launch {
            try {
                SessaoUsuario.exigirUsuario()
                op()
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Um erro inesperado aconteceu."
            } finally {
                carregando = false
            }
        }
    }

    fun listar() = executar {
        val lista = withContext(Dispatchers.IO) { dao.listar() }
        restaurantes = lista
    }

    fun buscarPorId(id: Long) {
        if (restaurante?.id == id) return
        executar {
            val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id) }
            restaurante = requireNotNull(encontrado) { "Restaurante não encontrado" }
            nome = encontrado.nome
            avaliacao = encontrado.avaliacao
            tempo = encontrado.tempo
            cep = encontrado.cep
            rua = encontrado.rua
            bairro = encontrado.bairro
            cidade = encontrado.cidade
            uf = encontrado.uf
        }
    }

    fun mudarNome(valor: String) {
        if (!carregando && valor.length <= 100) nome = valor
    }

    fun mudarAvaliacao(valor: String) {
        if (!carregando && valor.length <= 10) avaliacao = valor
    }

    fun mudarTempo(valor: String) {
        if (!carregando && valor.length <= 20) tempo = valor
    }

    fun mudarCep(valor: String) {
        if (consultandoCep || valor.length > 9) return
        cep = valor
        mensagem = null
    }

    fun mudarRua(valor: String) {
        if (!carregando && valor.length <= 120) rua = valor
    }

    fun mudarBairro(valor: String) {
        if (!carregando && valor.length <= 80) bairro = valor
    }

    fun mudarCidade(valor: String) {
        if (!carregando && valor.length <= 80) cidade = valor
    }

    fun mudarUf(valor: String) {
        if (!carregando && valor.length <= 2) uf = valor.uppercase()
    }

    /** Consulta ViaCEP e preenche rua, bairro, cidade e uf automaticamente. */
    fun consultarCep() {
        if (consultandoCep || carregando) return
        mensagem = null

        if (!Regex("[0-9]{5}-?[0-9]{3}").matches(cep)) {
            mensagem = "Informe um CEP válido"
            return
        }

        val cepConsulta = cep.replace("-", "")
        consultandoCep = true

        viewModelScope.launch {
            try {
                val resposta = ViaCepApi.service.buscar(cepConsulta)
                when {
                    resposta.erro == true -> mensagem = "CEP não encontrado"
                    resposta.cep.isNullOrBlank() -> mensagem = "Resposta inválida"
                    else -> {
                        cep = resposta.cep ?: cep
                        rua = resposta.rua.orEmpty()
                        bairro = resposta.bairro.orEmpty()
                        cidade = resposta.cidade.orEmpty()
                        uf = resposta.uf.orEmpty()
                        mensagem = null
                    }
                }
            } catch (erro: CancellationException) {
                throw erro
            } catch (_: Exception) {
                mensagem = "Não foi possível consultar o CEP"
            } finally {
                consultandoCep = false
            }
        }
    }

    fun cadastrar(
        nome: String,
        avaliacao: String,
        tempo: String,
        cep: String,
        rua: String,
        bairro: String,
        cidade: String,
        uf: String
    ) = executar {
        val nomeLimpo = nome.trim()
        val avaliacaoLimpa = avaliacao.trim()
        val tempoLimpo = tempo.trim()

        RestauranteValidator.validar(nomeLimpo, avaliacaoLimpa, tempoLimpo)
        withContext(Dispatchers.IO) {
            dao.cadastrar(
                nomeLimpo, avaliacaoLimpa, tempoLimpo,
                cep.trim(), rua.trim(), bairro.trim(), cidade.trim(), uf.trim()
            )
        }
        salvo = true
    }

    fun salvar() = executar {
        val selecionado = requireNotNull(restaurante) { "Selecione um restaurante" }
        RestauranteValidator.validar(nome, avaliacao, tempo)
        withContext(Dispatchers.IO) {
            dao.alterar(
                selecionado.id, nome, avaliacao, tempo,
                cep.trim(), rua.trim(), bairro.trim(), cidade.trim(), uf.trim()
            )
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        withContext(Dispatchers.IO) { dao.excluir(id) }
        restaurantes = restaurantes.filterNot { it.id == id }
        mensagem = "Restaurante excluído"
    }

    fun consumirSalvo() {
        salvo = false
    }

    fun limparFormulario() {
        nome = ""
        avaliacao = ""
        tempo = ""
        cep = ""
        rua = ""
        bairro = ""
        cidade = ""
        uf = ""
        mensagem = null
        restaurante = null
    }
}
