package com.example.delivery.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.delivery.data.local.AppDatabase
import com.example.delivery.data.model.Restaurante

internal class RestauranteDao(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(nome: String, avaliacao: String, tempo: String){
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("avaliacao", avaliacao)
                put("tempo", tempo)
            }
            db.writableDatabase
                .insertOrThrow("restaurantes", null, dados)
        }
    }

    fun listar(): List<Restaurante> = AppDatabase(appContext).use {
        db ->
        db.readableDatabase.query(
            "restaurantes",
            arrayOf("id", "nome", "avaliacao", "tempo"),
            null, null, null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()){
                    add(Restaurante(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)))
                }
            }
        }
    }

    fun buscarPorId(id: Long): Restaurante? = AppDatabase(appContext).use {
        db ->
        db.readableDatabase.query(
            "restaurantes",
            arrayOf("id", "nome", "avaliacao", "tempo"),
            "id = ?",
            arrayOf(id.toString()),
            null, null,null
        ).use { cursor ->
            if(cursor.moveToFirst()) Restaurante(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3))
            else null
        }
    }

    fun alterar(id: Long, nome: String, avaliacao: String, tempo: String){
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("avaliacao", avaliacao)
                put("tempo", tempo)
            }
            val alterados = db.writableDatabase.update(
                "restaurantes",
                dados,
                "id = ?",
                arrayOf(id.toString()))
            require(alterados == 1) {"Erro ao alterar o restaurante"}
        }
    }

    fun excluir(id: Long){
        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "restaurantes", "id = ?",
                arrayOf(id.toString())
            )
            require(excluidos == 1) {"Restaurante não pode ser excluído"}
        }
    }

}
