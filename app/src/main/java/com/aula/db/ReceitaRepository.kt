package com.aula.db

import android.content.Context
import com.aula.constants.RECEITAS_DB_NAME
import database
import org.jetbrains.anko.db.*
import timber.log.Timber


class ReceitaRepository(val context: Context) {

    fun findAll() : ArrayList<Receita> = context.database.use {
        val receitas = ArrayList<Receita>()

        select(RECEITAS_DB_NAME, "id",
            "email",
            "endereco",
            "nome",
            "telefone",
            "datanascimento",
            "site",
            "foto")
            .parseList(object: MapRowParser<List<Receita>> {
                override fun parseRow(columns: Map<String, Any?>): List<Receita> {
                    receitas.add(Receita(
                        id = columns.getValue("id").toString()?.toLong(),
                        foto = columns.getValue("foto")?.toString(),
                        nome = columns.getValue("nome")?.toString(),
                        endereco = columns.getValue("endereco")?.toString(),
                        telefone = columns.getValue("telefone")?.toString()?.toLong(),
                        dataNascimento = columns.getValue("datanascimento")?.toString()?.toLong(),
                        email = columns.getValue("email")?.toString(),
                        site = columns.getValue("site")?.toString()))
                    return receitas
                }
            })
        receitas
    }


    fun create(receita: Receita) = context.database.use {
        insert(RECEITAS_DB_NAME,
            "foto" to receita.foto,
            "nome" to receita.nome,
            "endereco" to receita.endereco,
            "telefone" to receita.telefone,
            "email" to receita.email,
            "site" to receita.site,
            "dataNascimento" to receita.dataNascimento)
    }

    fun update(receita: Receita) = context.database.use {
        val updateResult = update(RECEITAS_DB_NAME,
            "foto" to receita.foto,
            "nome" to receita.nome,
            "endereco" to receita.endereco,
            "telefone" to receita.telefone,
            "email" to receita.email,
            "site" to receita.site)
            .whereArgs("id = {id}","id" to receita.id).exec()
        Timber.d("Update result code is $updateResult")
    }

    fun delete(id: Long) = context.database.use {
        delete(RECEITAS_DB_NAME, "id = {receitaId}", args = *arrayOf("receitaId" to id))
    }

    fun findAll(filter: String) : ArrayList<Receita> = context.database.use {
        val receitas = ArrayList<Receita>()

        select(RECEITAS_DB_NAME, "id", "email", "endereco", "nome", "telefone", "datanascimento", "site", "foto")
            .whereArgs( "nome like {nome}", "nome" to filter)
            .parseList(object: MapRowParser<List<Receita>> {
                override fun parseRow(columns: Map<String, Any?>): List<Receita> {
                    val id = columns.getValue("id")
                    val email = columns.getValue("email")
                    val endereco = columns.getValue("endereco")
                    val nome = columns.getValue("nome")
                    val telefone = columns.getValue("telefone")
                    val datanascimento = columns.getValue("datanascimento")
                    val site = columns.getValue("site")
                    val foto = columns.getValue("foto")

                    receitas.add(Receita(
                        id.toString()?.toLong(),
                        foto?.toString(),
                        nome?.toString(),
                        endereco?.toString(),
                        telefone?.toString()?.toLong(),
                        datanascimento?.toString()?.toLong(),
                        email?.toString(),
                        site?.toString()))
                    return receitas
                }
            })

        receitas
    }
}