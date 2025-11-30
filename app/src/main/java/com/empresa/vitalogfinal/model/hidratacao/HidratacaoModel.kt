package com.empresa.vitalogfinal.model.hidratacao

data class HidratacaoModel(
    val id: Int,
    val usuario_id: Int,
    val quantidade: Double, // Double aceita o DECIMAL(10,2) perfeitamente
    val data_registro: String
)
