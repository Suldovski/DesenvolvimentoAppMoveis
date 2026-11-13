package com.example.delivery.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.delivery.data.local.AppDatabase
import com.example.delivery.data.model.Categoria

internal class CategoriaDao(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(nome: String){
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
            }
            db.writableDatabase
                .insertOrThrow("categorias", null, dados)
        }
    }

    fun listar(): List<Categoria> = AppDatabase(appContext).use {
        db ->
        db.readableDatabase.query(
            "categorias",
            arrayOf("id", "nome"),
            null, null, null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()){
                    add(Categoria(cursor.getLong(0),
                        cursor.getString(1)))
                }
            }
        }
    }

    fun buscarPorId(id: Long): Categoria? = AppDatabase(appContext).use {
        db ->
        db.readableDatabase.query(
            "categorias",
            arrayOf("id", "nome"),
            "id = ?",
            arrayOf(id.toString()),
            null, null,null
        ).use { cursor ->
            if(cursor.moveToFirst()) Categoria(cursor.getLong(0),
                cursor.getString(1))
            else null
        }
    }

    fun alterar(id: Long, nome: String){
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
            }
            val alterados = db.writableDatabase.update(
                "categorias",
                dados,
                "id = ?",
                arrayOf(id.toString()))
            require(alterados == 1) {"Erro ao alterar a categoria"}
        }
    }

    fun excluir(id: Long){
        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "categorias", "id = ?",
                arrayOf(id.toString())
            )
            require(excluidos == 1) {"Categoria não pode ser excluída"}
        }
    }

}
