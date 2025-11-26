package com.empresa.vitalogfinal.model.alimento

class Alimento {
    private var id : Int
        get() {
            TODO()
        }
        set(value) {}
    private var nome : String
        get() {
            TODO()
        }
        set(value) {}
    private var porcao : String
        get() {
            TODO()
        }
        set(value) {}
    private var caloria : String
        get() {
            TODO()
        }
        set(value) {}
    constructor()
    constructor(id: Int, nome: String, porcao: String, caloria: String) {
        this.id = id
        this.nome = nome
        this.porcao = porcao
        this.caloria = caloria
    }
}