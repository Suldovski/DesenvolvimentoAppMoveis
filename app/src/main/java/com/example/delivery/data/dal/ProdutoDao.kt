package com.example.delivery.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.delivery.data.local.AppDatabase
import com.example.delivery.data.model.Produto

internal class ProdutoDao(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(nome: String, preco: String, categoriaId: Long){
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("preco", preco)
                put("categoriaId", categoriaId)
            }
            db.writableDatabase
                .insertOrThrow("produtos", null, dados)
        }
    }

    fun listar(): List<Produto> = AppDatabase(appContext).use {
        db ->
        db.readableDatabase.query(
            "produtos",
            arrayOf("id", "nome", "preco", "categoriaId"),
            null, null, null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()){
                    add(Produto(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3)))
                }
            }
        }
    }

    fun buscarPorId(id: Long): Produto? = AppDatabase(appContext).use {
        db ->
        db.readableDatabase.query(
            "produtos",
            arrayOf("id", "nome", "preco", "categoriaId"),
            "id = ?",
            arrayOf(id.toString()),
            null, null,null
        ).use { cursor ->
            if(cursor.moveToFirst()) Produto(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getLong(3))
            else null
        }
    }

    fun alterar(id: Long, nome: String, preco: String, categoriaId: Long){
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("preco", preco)
                put("categoriaId", categoriaId)
            }
            val alterados = db.writableDatabase.update(
                "produtos",
                dados,
                "id = ?",
                arrayOf(id.toString()))
            require(alterados == 1) {"Erro ao alterar o produto"}
        }
    }

    fun excluir(id: Long){
        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "produtos", "id = ?",
                arrayOf(id.toString())
            )
            require(excluidos == 1) {"Produto não pode ser excluído"}
        }
    }

}
