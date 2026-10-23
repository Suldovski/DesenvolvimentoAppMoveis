package com.example.delivery.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

internal class AppDatabase(context: Context): SQLiteOpenHelper(
    context.applicationContext, File(context.noBackupFilesDir,
        "delivery.db").absolutePath,
    null, 1
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
            tempo TEXT NOT NULL CHECK(length(tempo) BETWEEN 1 AND 20)
            )
        """.trimIndent())

        db.execSQL("INSERT INTO restaurantes (nome, avaliacao, tempo) VALUES ('Burger House', '4.8', '25-35 min')")
        db.execSQL("INSERT INTO restaurantes (nome, avaliacao, tempo) VALUES ('Pizza Express', '4.7', '30-40 min')")
        db.execSQL("INSERT INTO restaurantes (nome, avaliacao, tempo) VALUES ('Sushi Temaki', '4.6', '35-45 min')")
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }

}
