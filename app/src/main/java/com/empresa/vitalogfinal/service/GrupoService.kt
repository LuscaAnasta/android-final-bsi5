package com.empresa.vitalogfinal.service


import com.empresa.vitalogfinal.model.diario.FoodModel
import com.empresa.vitalogfinal.model.diario.GrupoModel
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface GrupoService {

    @GET("grupo/{id}")
    suspend fun getGrupo(@Path("id") id: Int): Response<GrupoModel>

    @GET("grupo/{id}/alimentos")
    suspend fun getAlimentos(@Path("id") id: Int): Response<List<FoodModel>>

    @DELETE("grupo/{id}")
    suspend fun deleteGrupo(@Path("id") id: Int): Response<Void>

    @DELETE("alimento/{id}")
    suspend fun deleteAlimento(@Path("id") id: Int): Response<Void>
}