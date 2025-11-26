package com.empresa.vitalogfinal.view.menu.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.empresa.vitalogfinal.model.diario.GrupoModel
import com.empresa.vitalogfinal.repository.DiarioRepository

class DiarioViewModel(
    private val repository: DiarioRepository
) : ViewModel() {

    private val _grupos = MutableLiveData<List<GrupoModel>>()
    val grupos: LiveData<List<GrupoModel>> = _grupos

    suspend fun carregarDiario(usuarioId: Int, data: String) {
        val result = repository.getDiario(usuarioId, data)
        _grupos.postValue(result ?: emptyList())
    }

    suspend fun criarGrupo(usuarioId: Int, nome: String): GrupoModel? {
        return repository.criarGrupo(usuarioId, nome)
    }

    // Atualiza a lista localmente
    fun adicionarGrupoLocal(grupo: GrupoModel) {
        _grupos.value = _grupos.value.orEmpty() + grupo
    }
}