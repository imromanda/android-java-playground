package com.example.blocdenotas_1_7;
//"Esta clase pertenece a este paquete del proyecto"

import android.content.Context;
//Importa la clase Context, una de las más importantes de Android
//Importa Context para acceder al entorno de la aplicación
import android.database.sqlite.SQLiteDatabase;
//Importa la clase que representa la base de datos SQLite
import android.database.sqlite.SQLiteOpenHelper;
//clase base que Android proporciona para AYUDAR A GESTIONAR bases de datos SQLite
//Mi clase NotesDatabaseHelper HEREDARÁ de SQLiteOpenHelper;
public class NotesDatabaseHelper extends SQLiteOpenHelper {
//clase accesible dese cualquier parte de la app que HEREDARÁ DE (extends) SQLiteOpenHelper
    public static final String DATABASE_NAME = "notes.db";
  // La constante es accesible desde cualquier parte de la app
  // static = constante pertenece a la clase y no a un objeto específico
  // El valor de la constante no puede modificarse (aka constante & FINAL "notes.db" queda FIJO)
  // CONSTANTE con un solo valor string llamada DATABASE_NAME =
  // Por convención: constantes → MAYÚSCULAS_SEPARADOR_BARRA_BAJA
    //"notes.db" Archivo físico SQLite que almacenará las notas

  public static final int DATABASE_VERSION = 1;
//Define la versión de la base de datos
// Public Constante accesible desde otras clases
// Static pertenece a la clase y no a un objeto específico
// El valor no puede modificarse
// Tipo de dato entero
// Versión actual de la base de datos
// Primera versión de la estructura SQLite

    public static final String TABLE_NOTES = "notes";
  // Nombre de la tabla que almacenará las notas
  // Se usa una constante para evitar errores tipográficos
    public static final String COLUMN_ID = "id";
    // Nombre de la columna que almacenará los ids de cada nota

    public static final String COLUMN_TITLE = "title";
    // Nombre de la columna que almacenará los títulos de cada nota

    public static final String COLUMN_CONTENT = "content";
    // Nombre de la columna que almacenará el contenido de cada nota


    private static final String SQL_CREATE_TABLE =
    //"Crea la tabla de notas con sus columnas"
    // Constante privada usada solo dentro del helper SQLite
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT" +
                    ");";
    //

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NOTES;

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}
