package com.aula.db

import java.io.Serializable

data class Receita(
    var id: Long = 0,
    var foto: String? = null,
    var nome: String? = null,
    var autor: String? = null,
    var email: String? = null,
    var descricao: String? = null,
    var ingredientes: String? = null,
    var modopreparo: String? = null) : Serializable {

    override fun toString(): String {
        return nome.toString()
    }
}
