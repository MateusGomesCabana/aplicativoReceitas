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
            "foto",
            "nome",
            "autor",
            "email",
            "descricao",
            "ingredientes",
            "modoPreparo")
            .parseList(object: MapRowParser<List<Receita>> {
                override fun parseRow(columns: Map<String, Any?>): List<Receita> {
                    receitas.add(Receita(
                        id = columns.getValue("id").toString()?.toLong(),
                        foto = columns.getValue("foto")?.toString(),
                        nome = columns.getValue("nome")?.toString(),
                        autor = columns.getValue("autor")?.toString(),
                        email = columns.getValue("email")?.toString(),
                        descricao = columns.getValue("descricao")?.toString(),
                        ingredientes = columns.getValue("ingredientes")?.toString(),
                        modopreparo = columns.getValue("modopreparo")?.toString()))
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
            "email" to receita.email,
            "descricao" to receita.descricao,
            "ingredientes" to receita.ingredientes,
            "modoPreparo" to receita.modopreparo)
    }

    fun update(receita: Receita) = context.database.use {
        val updateResult = update(RECEITAS_DB_NAME,
            "foto" to receita.foto,
            "nome" to receita.nome,
            "autor" to receita.autor,
            "email" to receita.email,
            "descricao" to receita.descricao,
            "ingredientes" to receita.ingredientes,
            "modopreparo" to receita.modopreparo)
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
                    val foto = columns.getValue("foto")
                    val nome = columns.getValue("nome")
                    val autor = columns.getValue("autor")
                    val email = columns.getValue("email")
                    val descricao = columns.getValue("descricao")
                    val ingredientes = columns.getValue("ingredientes")
                    val modopreparo = columns.getValue("modopreparo")


                    receitas.add(Receita(
                        id.toString()?.toLong(),
                        foto?.toString(),
                        nome?.toString(),
                        autor?.toString(),
                        email?.toString(),
                        descricao?.toString(),
                        ingredientes?.toString(),
                        modopreparo?.toString()))
                    return receitas
                }
            })

        receitas
    }
}