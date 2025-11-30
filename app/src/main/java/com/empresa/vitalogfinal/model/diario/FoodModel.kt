package com.empresa.vitalogfinal.model.diario

data class FoodModel(
    val id: Int,
    val usuario_id: Int, // <--- OBRIGATÓRIO: O Node.js exige isso
    val grupo_id: Int,
    val nome: String,
    val caloria_base: Double,
    val porcao_consumida: Double,
    val porcao_base: Double,
    val data_registro: String
)