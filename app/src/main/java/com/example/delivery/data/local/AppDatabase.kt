package com.example.delivery.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

internal class AppDatabase(context: Context): SQLiteOpenHelper(
    context.applicationContext, File(context.noBackupFilesDir,
        "delivery.db").absolutePath,
    null, 3
){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuarios(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL CHECK(length(nome) BETWEEN 3 AND 100),
            email TEXT NOT NULL UNIQUE CHECK(length(email) BETWEEN 3 AND 254),
            senha BLOB NOT NULL,
            salt BLOB NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE restaurantes(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL CHECK(length(nome) BETWEEN 3 AND 100),
            avaliacao TEXT NOT NULL,
            tempo TEXT NOT NULL CHECK(length(tempo) BETWEEN 1 AND 20),
            cep TEXT DEFAULT '',
            rua TEXT DEFAULT '',
            bairro TEXT DEFAULT '',
            cidade TEXT DEFAULT '',
            uf TEXT DEFAULT ''
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE categorias(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL CHECK(length(nome) BETWEEN 2 AND 50)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE produtos(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL CHECK(length(nome) BETWEEN 3 AND 100),
            preco TEXT NOT NULL,
            categoriaId INTEGER,
            FOREIGN KEY(categoriaId) REFERENCES categorias(id)
            )
        """.trimIndent())

        db.execSQL("INSERT INTO restaurantes (nome, avaliacao, tempo, cep, rua, bairro, cidade, uf) VALUES ('Burger House', '4.8', '25-35 min', '01310-100', 'Av. Paulista', 'Bela Vista', 'São Paulo', 'SP')")
        db.execSQL("INSERT INTO restaurantes (nome, avaliacao, tempo, cep, rua, bairro, cidade, uf) VALUES ('Pizza Express', '4.7', '30-40 min', '20040-020', 'Av. Rio Branco', 'Centro', 'Rio de Janeiro', 'RJ')")
        db.execSQL("INSERT INTO restaurantes (nome, avaliacao, tempo, cep, rua, bairro, cidade, uf) VALUES ('Sushi Temaki', '4.6', '35-45 min', '30130-010', 'Av. Afonso Pena', 'Centro', 'Belo Horizonte', 'MG')")

        db.execSQL("INSERT INTO categorias (nome) VALUES ('Lanches')")
        db.execSQL("INSERT INTO categorias (nome) VALUES ('Pizzas')")
        db.execSQL("INSERT INTO categorias (nome) VALUES ('Japonesa')")

        db.execSQL("INSERT INTO produtos (nome, preco, categoriaId) VALUES ('X-Burger', '25.90', 1)")
        db.execSQL("INSERT INTO produtos (nome, preco, categoriaId) VALUES ('Pizza Calabresa', '45.00', 2)")
        db.execSQL("INSERT INTO produtos (nome, preco, categoriaId) VALUES ('Temaki Salmão', '32.50', 3)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("""
                CREATE TABLE IF NOT EXISTS categorias(
                id INTEGER PRIMARY KEY,
                nome TEXT NOT NULL CHECK(length(nome) BETWEEN 2 AND 50)
                )
            """.trimIndent())
            db?.execSQL("""
                CREATE TABLE IF NOT EXISTS produtos(
                id INTEGER PRIMARY KEY,
                nome TEXT NOT NULL CHECK(length(nome) BETWEEN 3 AND 100),
                preco TEXT NOT NULL,
                categoriaId INTEGER,
                FOREIGN KEY(categoriaId) REFERENCES categorias(id)
                )
            """.trimIndent())
        }
        if (oldVersion < 3) {
            try {
                db?.execSQL("ALTER TABLE restaurantes ADD COLUMN cep TEXT DEFAULT ''")
                db?.execSQL("ALTER TABLE restaurantes ADD COLUMN rua TEXT DEFAULT ''")
                db?.execSQL("ALTER TABLE restaurantes ADD COLUMN bairro TEXT DEFAULT ''")
                db?.execSQL("ALTER TABLE restaurantes ADD COLUMN cidade TEXT DEFAULT ''")
                db?.execSQL("ALTER TABLE restaurantes ADD COLUMN uf TEXT DEFAULT ''")
            } catch (_: Exception) { }
        }
    }
}
