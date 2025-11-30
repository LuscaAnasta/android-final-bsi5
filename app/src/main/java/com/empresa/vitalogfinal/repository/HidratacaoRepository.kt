package com.empresa.vitalogfinal.repository

import com.empresa.vitalogfinal.model.hidratacao.HidratacaoModel
import com.empresa.vitalogfinal.model.hidratacao.HidratacaoRequest // <--- Importe aqui
import com.empresa.vitalogfinal.service.HidratacaoService

class HidratacaoRepository(private val api: HidratacaoService) {

    suspend fun listar(usuarioId: Int, data: String): List<HidratacaoModel> {
        val res = api.listar(usuarioId, data)
        return if (res.isSuccessful) res.body() ?: emptyList() else emptyList()
    }

    suspend fun adicionar(usuarioId: Int, quantidade: Double): Boolean {
        // CORREÇÃO AQUI: Criamos o objeto tipado em vez do Map
        val request = HidratacaoRequest(
            usuario_id = usuarioId,
            quantidade = quantidade
        )
        return api.adicionar(request).isSuccessful
    }

    suspend fun remover(id: Int): Boolean {
        return api.remover(id).isSuccessful
    }
}