package com.empresa.vitalogfinal.model.meta

data class MetaModel(
    val id: Int = 0, // Pode ser 0 se for uma meta padrão não salva ainda
    val usuario_id: Int,
    val tipo: String, // "caloria" ou "hidratacao"
    val meta: Double,
    val data_registro: String? = null
)