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


    // Consulta SQL para eliminar la tabla de notas
    private static final String SQL_DROP_TABLE =
            // Instrucción SQL para eliminar la tabla solo si existe
            // Nombre de la tabla definido mediante constante
            "DROP TABLE IF EXISTS " + TABLE_NOTES;

    //"Crear un helper para gestionar la base de datos"
    // Constructor de la clase NotesDatabaseHelper
    //Constructores siempre tienen el mismo nombre que las clases
    //Recibe un context
    public NotesDatabaseHelper(Context context) {
    // Llama al constructor de SQLiteOpenHelper(al padre con el super)
    super(
        // contexto de la aplicación
        // Nombre del archivo de base de datos
        // Se usa la fábrica por defecto de SQLite
        // Versión actual de la base de datos
            context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //"Qué hacer cuando se crea la base de datos por primera vez"
    public void onCreate(SQLiteDatabase db)
            //El méetodo recibe un objeto SQLiteDatabase.
    {
        //"Ejecuta el SQL para crear la tabla de notas"
        //db = objeto SQLiteDatabase recibido en onCreate()
        //execSQL = Ejecuta una instrucción SQL directamente

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    //"Qué hacer cuando se guarde una nueva versión de la base de datos"
    //"Qué hacer cuando la base de datos necesita actualizarse"
    //Conexión activa SQLite.
    public void onUpgrade(
            //Conexión activa SQLite,
            //número de la anterior versión,
            //número de la nueva versión newVersion
            SQLiteDatabase db, int oldVersion, int newVersion)
        //
    {
        //"Eliminar la tabla anterior de notas"
        db.execSQL(SQL_DROP_TABLE);
        //Vuelve a ejecutar el méetodo onCreate() para crear otra vez las tablas
        onCreate(db);
    }
}
