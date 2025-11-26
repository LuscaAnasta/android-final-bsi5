package com.empresa.vitalogfinal.service

import com.empresa.vitalogfinal.model.usuario.CadastroRequest
import com.empresa.vitalogfinal.model.usuario.CadastroResponse
import com.empresa.vitalogfinal.model.usuario.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsuarioService {

    @GET("usuario/login")
    fun login(
        @Query("email") email: String,
        @Query("senha") senha: String
    ): Call<LoginResponse>

    @POST("usuario/cadastro")
    fun cadastro(
        @Body request: CadastroRequest
    ): Call<CadastroResponse>
}

