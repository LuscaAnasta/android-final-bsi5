package com.empresa.vitalogfinal.model.usuario

data class Usuario(
    val id: Int,
    val nome: String,
    val sobrenome: String,
    val email: String,
    val senha: String,
    val cpf: String,
    val data_nascimento: String
)