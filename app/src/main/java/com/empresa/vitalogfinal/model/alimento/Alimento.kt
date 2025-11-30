package com.empresa.vitalogfinal.model.alimento

import com.google.gson.annotations.SerializedName

// Este model representa o resultado da pesquisa na tabela base (catálogo)
data class Alimento(
    val id: Int,
    val nome: String,

    // Usamos Double pois faremos contas matemáticas
    // O SerializedName garante que o Retrofit entenda mesmo se o JSON vier com outro nome
    @SerializedName("caloria", alternate = ["caloria_base", "calorias"])
    val caloria: Double,

    @SerializedName("porcao", alternate = ["porcao_base", "tamanho_porcao"])
    val porcao: Double
)