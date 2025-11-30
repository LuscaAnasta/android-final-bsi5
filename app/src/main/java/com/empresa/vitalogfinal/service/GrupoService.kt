package com.empresa.vitalogfinal.service

import com.empresa.vitalogfinal.model.diario.FoodModel
import com.empresa.vitalogfinal.model.diario.GrupoModel
import okhttp3.ResponseBody // <--- IMPORTANTE: Adicione esta linha
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface GrupoService {

    // AVISO: Verifique se você criou a rota GET '/grupo/:id' no Node.js.
    // No código que você me mandou antes, essa rota NÃO existia.
    @GET("grupo/{id}")
    suspend fun getGrupo(@Path("id") id: Int): Response<GrupoModel>

    // Corrigido para bater com seu Node.js: app.get('/diario/grupo/:id/alimentos'...)
    @GET("diario/grupo/{id}/alimentos")
    suspend fun getAlimentos(@Path("id") id: Int): Response<List<FoodModel>>

    // Corrigido para bater com seu Node.js: app.delete('/diario/grupo/:id'...)
    // E mudado para ResponseBody para evitar erro de JSON vazio
    @DELETE("diario/grupo/{id}")
    suspend fun deleteGrupo(@Path("id") id: Int): Response<ResponseBody>

    // Essa rota parece correta com seu Node (app.delete('/alimento/:id'))
    @DELETE("alimento/{id}")
    suspend fun deleteAlimento(@Path("id") id: Int): Response<ResponseBody>
}