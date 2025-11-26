package com.empresa.vitalogfinal.repository

import com.empresa.vitalogfinal.model.diario.CriarGrupoRequest
import com.empresa.vitalogfinal.model.diario.GrupoModel
import com.empresa.vitalogfinal.service.DiarioService

class DiarioRepository(
    private val diarioService: DiarioService
) {

    suspend fun getDiario(usuarioId: Int, data: String): List<GrupoModel>? {
        val response = diarioService.getDiario(usuarioId, data)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun criarGrupo(usuarioId: Int, nome: String): GrupoModel? {
        val request = CriarGrupoRequest(nome)
        val response = diarioService.criarGrupo(usuarioId, request)
        return if (response.isSuccessful) response.body() else null
    }

}
