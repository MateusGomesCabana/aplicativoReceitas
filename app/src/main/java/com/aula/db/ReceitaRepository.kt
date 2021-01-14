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
            "nome",
            "autor",
            "ingredientes",
            "modoPreparo",
            "data",
            "foto")
            .parseList(object: MapRowParser<List<Receita>> {
                override fun parseRow(columns: Map<String, Any?>): List<Receita> {
                    receitas.add(Receita(
                        id = columns.getValue("id").toString()?.toLong(),
                        foto = columns.getValue("foto")?.toString(),
                        nome = columns.getValue("nome")?.toString(),
                        autor = columns.getValue("autor")?.toString(),
                        ingredientes = columns.getValue("ingredientes")?.toString(),
                        modopreparo = columns.getValue("modopreparo")?.toString(),
                        data = columns.getValue("data")?.toString()?.toLong()))
                    return receitas
                }
            })
        receitas
    }


    fun create(receita: Receita) = context.database.use {
        insert(RECEITAS_DB_NAME,
            "foto" to receita.foto,
            "nome" to receita.nome,
            "autor" to receita.autor,
            "ingredientes" to receita.ingredientes,
            "modoPreparo" to receita.modopreparo,
            "data" to receita.data)
    }

    fun update(receita: Receita) = context.database.use {
        val updateResult = update(RECEITAS_DB_NAME,
            "foto" to receita.foto,
            "nome" to receita.nome,
            "autor" to receita.autor,
            "ingredientes" to receita.ingredientes,
            "modopreparo" to receita.modopreparo,
            "data" to receita.data)
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
                    val nome = columns.getValue("nome")
                    val autor = columns.getValue("autor")
                    val ingredientes = columns.getValue("ingredientes")
                    val modopreparo = columns.getValue("modopreparo")
                    val data = columns.getValue("data")
                    val foto = columns.getValue("foto")

                    receitas.add(Receita(
                        id.toString()?.toLong(),
                        foto?.toString(),
                        nome?.toString(),
                        autor?.toString(),
                        ingredientes?.toString(),
                        modopreparo?.toString(),
                        data?.toString()?.toLong()))
                    return receitas
                }
            })

        receitas
    }
}