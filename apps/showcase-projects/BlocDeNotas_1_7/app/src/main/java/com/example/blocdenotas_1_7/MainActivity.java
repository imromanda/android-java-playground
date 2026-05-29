package com.example.blocdenotas_1_7;
// Paquete donde está organizada esta clase dentro del proyecto


import androidx.appcompat.app.AppCompatActivity;
// Clase base para crear una pantalla de Android
//Esto permite también
//Trabaja con versiones antiguas

import android.graphics.Typeface;
// Permite cambiar el estilo de la fuente (normal, negrita, cursiva)

import android.os.Bundle;
// Contenedor de datos usado al crear la Activity
//Permite acceder directamente a los elementos del "DOM"

import android.widget.ArrayAdapter;
// Adaptador para mostrar arrays en componentes visuales como Spinner

import android.widget.AdapterView;
// Permite detectar selecciones en componentes como Spinner

import android.content.SharedPreferences;
// Permite guardar configuraciones simples de forma permanente

import com.example.blocdenotas_1_7.databinding.ActivityMainBinding;
// ViewBinding: permite acceder fácilmente a las vistas del layout

// imports nuevos
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
// Pantalla principal de la aplicación con "extends" HEREDA el comportamiento de una Activity de Android

    private ActivityMainBinding binding;
    //Creamos variable binding que es un objeto
    // Objeto ViewBinding para acceder a las vistas del layout fácilmente


    // Constantes para SharedPreferences
    //Varias variables privadas = Solo para esta clase
    //Static = Pertenecen a la clase
    //Final = No van a cambiar


    private static final String PREFS_NAME = "AccesibilidadPrefs";
    // Nombre del archivo donde se guardan las preferencias

    private static final String KEY_PROFILE = "accessibility_profile";
    // Clave usada para guardar el tamaño de fuente seleccionado: normal, grande o muy grande

    private static final String KEY_FONT_STYLE = "font_style";
    // Clave usada para guardar el estilo de fuente seleccionado: normal, negrita o cursiva


    // dentro de MainActivity
    private NotesDatabaseHelper dbHelper;
    private ArrayAdapter<String> notesAdapter;
    private List<String> notesTitles = new ArrayList<>();




    @Override
    // Sobrescribe el metodo onCreate de AppCompatActivity

    protected void onCreate(Bundle savedInstanceState) {
    // Protected = nivel de acceso = esta clase puede usarlo, clases hijas también
    // Void = no devuelve nada
    // onCreate = se ejecuta cuando la app se abre por primera vez
    //Bundle savedInstanceState = Es información guardada anteriormente si la Activity se recrea

    super.onCreate(savedInstanceState);
        //Línea vital, sin ella no funcionaría nada
        // Ejecuta el onCreate de la clase padre para inicializar la Activity


        //Inflar el layout con ViewBinding
        //Inflar = convertir el XML visual en objetos Java utilizables.
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Crea el objeto binding y conecta el XML con el código Java
        setContentView(binding.getRoot());
        // Muestra en pantalla la interfaz creada desde el XML principal


        //Estas líneas organizan la inicialización de la app:
        setupAccessibilitySpinner();
        loadAccessibilityProfile();
        setupFontStyleSpinner();
        loadFontStyle();
    //En vez de meter tooodo el contenido dentro de onCreate(), lo divides en métodos más pequeños.

        // Base de datos
        dbHelper = new NotesDatabaseHelper(this);

        notesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                notesTitles
        );
        binding.lvNotes.setAdapter(notesAdapter);

        binding.btnSaveToDb.setOnClickListener(v -> saveNoteToDatabase());

        loadNotesFromDatabase();

    } //AQUÍ ACABA EL METODO ONCREATE
    private void saveNoteToDatabase() {
        String title = binding.etTitle.getText().toString().trim();
        String content = binding.etContent.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotesDatabaseHelper.COLUMN_TITLE, title);
        values.put(NotesDatabaseHelper.COLUMN_CONTENT, content);

        long newRowId = db.insert(NotesDatabaseHelper.TABLE_NOTES, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Nota guardada en la base de datos", Toast.LENGTH_SHORT).show();
            loadNotesFromDatabase();
        } else {
            Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNotesFromDatabase() {
        notesTitles.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                NotesDatabaseHelper.COLUMN_ID,
                NotesDatabaseHelper.COLUMN_TITLE
        };

        Cursor cursor = db.query(
                NotesDatabaseHelper.TABLE_NOTES,
                projection,
                null,
                null,
                null,
                null,
                NotesDatabaseHelper.COLUMN_ID + " DESC"
        );

        while (cursor.moveToNext()) {
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_TITLE));
            notesTitles.add(title);
        }
        cursor.close();

        notesAdapter.notifyDataSetChanged();
    }
    // Resto del código




    /* COMENZAMOS A CONFIGURAR EL SPINNER DE ACCESIBILIDAD */

    // Este méetodo solo puede utilizarse dentro de esta clase
    // El méetodo realiza acciones pero no devuelve ningún dato
    // Méetodo que configura el Spinner de accesibilidad
    // No recibe parámetros externos
    // Inicio del bloque de configuración del Spinner
    private void setupAccessibilitySpinner() {
        //Dentro tiene un array de strings llamado profiles, cuyo contenido es normal, grande y muy grande
        String[] profiles = {"Normal", "Grande", "Muy grande"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
        //ArrayAdapter es una clase Android que conecta los datos del array con el Spinner
        // Porque el spinner no puede acceder directamente al array
        //adapter es la variable que guardará el objeto adaptador
                /*ArrayAdapter necesita:
                        - contexto
                        - diseño visual
                        - datos*/
                    //A partir de aquí son los parámetros del constructor ArrayAdapter:
                this, // esta clase u objeto actual - CONTEXTO - “Usa esta pantalla actual como contexto”
                android.R.layout.simple_spinner_item,// DISEÑO VISUAL - “Qué diseño visual tendrá cada elemento del Spinner”
                profiles // Variable con los DATOS para rellenar el spinner
        );
        //Configura cómo se verá el menú desplegable DESPLEGADO:
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // .setDropDownViewResource()
        // Méetodo que configura el layout del desplegable
    binding.spAccessibilityProfile.setAdapter(adapter);
    // Objeto que da acceso a las vistas del layout
    // Referencia al Spinner de perfiles de accesibilidad
    // Asignamos el adaptador al Spinner
    // El Spinner usará el adapter para mostrar las opciones
    binding.spAccessibilityProfile.setOnItemSelectedListener(
        // Aquí se empiezan a escuchar los eventos en el spinner
        /*set	configurar
        OnItemSelected	cuando se selecciona un elemento
        Listener	escuchador/evento*/

        // DENTRO DEL setOnItemSelectedListener va toodo esto:
        new AdapterView.OnItemSelectedListener() {
          // "Crea un escuchador para reaccionar a las selecciones del Spinner"
          // new = Se crea un nuevo objeto listener
          // AdapterView = Clase base para componentes que usan adapters
          // OnItemSelectedListener = Listener que detecta selecciones en el Spinner
          @Override // Sobrescribimos el méetodo del listener para definir nuestro comportamiento
          // public ANDROID EN LISTENER EXIGE PUBLIC
          // onItemSelected = Méetodo que se ejecuta cuando el usuario selecciona una opción
          // Guardar el perfil seleccionado en SharedPreferences
          // Entre paréntesis los parámetros
          public void onItemSelected(
              AdapterView<?> parent,
              // AdapterView<?> parent representa el componente que lanzó el evento (Spinner)
              // <?> wildcard = "No importa el tipo exacto"
              // parent = spinner donde están los elementos
              android.view.View view,
              // android.view.View view, = Vista visual del elemento seleccionado
              int position,
              // int position, = Posición del elemento seleccionado dentro del Spinner
              long id) {
            // long id = ID del elemento seleccionado
            saveAccessibilityProfile(position);
            //Guarda el perfil de accesibilidad y le mandamos la posición seleccionada por el user
            applyFontSizeProfile(position);
            // Aplica inmediatamente el tamaño de fuente elegido y guardado en "position"
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {
              //onNothingSelected es un méetodo OBLIGATORIO del LISTENER
            // Cuando no se selecciona nada, no pasa nada
          }
        });
    } //FIN DE setupAccessibilitySpinner



    /* CÓMO SE VA A GUARDAR EL PERFIL DE ACCESIBILIDAD SELECCIONADO en SharedPreferences. */
    private void saveAccessibilityProfile(int index) {
    //private = solo puede usarse dentro de esta clase
    //Méetodo que guarda el perfil de accesibilidad seleccionado(nro. seleccionado en el spinner)

        SharedPreferences prefs =
        //Almacenamiento LIGERO clave = valor
                                //profile = 2
        // prefs = Variable que almacenará las preferencias de la aplicación
                getSharedPreferences(
                //Obtener/crear SharedPreferences
                        PREFS_NAME, MODE_PRIVATE);
                        // PREFS_NAME = Nombre del archivo donde se guardarán las preferencias
                        // MODE_PRIVATE = Modo de acceso al archivo, solo esta aplicación puede acceder al archivo


        prefs.edit()
        //Este bloque está guardando un número entero dentro de SharedPreferences
        // Usamos las preferencias(prefs) obtenidas anteriormente
        //.edit() Abre el modo edición de SharedPreferences

                .putInt(KEY_PROFILE, index)
                //guardar entero (clave, valor)
                .apply();
                // ¡IMPORTANTE!! Confirma y guarda los cambios, sin el apply los datos NO SE GUARDAN
    }//Fin de saveAccessibilityProfile


/*CARGA DEL PERFIL DE ACCESIBILIDAD */
    private void loadAccessibilityProfile() {
        // Méetodo que carga el perfil de accesibilidad guardado
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //Accede al archivo de preferencias guardadas (nombre del archivo,
        int index = prefs.getInt(KEY_PROFILE, 0); // 0 = Normal por defecto
        //int index = Variable entera que almacenará el perfil recuperado
        //prefs. = Usamos las preferencias previamente obtenidas
        //KEY_PROFILE Y dev value = Clave usada para recuperar el perfil guardado
        //defValue: 0 = si no hay nada guardado devuelve 0
        binding.spAccessibilityProfile.setSelection(index);
        //Busca la vista del Spinner y selecciona la opción (usando el índice)
        //Con esta línea el spinner recuerda y muestra la selección previa del usuario
        applyFontSizeProfile(index);
        //Aplica el tamaño al iniciar

    }//Fin loadAccessibilityProfile


    /* FUNCIÓN DONDE SE DEFINEN LOS TAMAÑOS DE FUENTE */
    private void applyFontSizeProfile(int index) {
    //función privada y no da respuesta para aplicar los tamaños de fuente al perfil (con el índice)
        //Declaramos las variables que almacenarán los tamaños de texto, reservadas, sin datos aún
        //float = número decimal, necesita 1234f detrás

        float headerSize;
        float titleSize;
        float contentSize;

        // Definimos tamaños según el perfil
        switch (index) {
            case 1: // Grande
                headerSize = 24f;
                titleSize = 20f;
                contentSize = 18f;
                break;
            case 2: // Muy grande
                headerSize = 28f;
                titleSize = 24f;
                contentSize = 22f;
                break;
            case 0:
            default: // Normal
                headerSize = 20f;
                titleSize = 16f;
                contentSize = 14f;
                break;
        }
        //Encuentra las vistas y cambia el tamaño del texto al que corresponda en cada casuística
        //"Aplica el tamaño configurado a cada texto"
        binding.tvHeader.setTextSize(headerSize);
        binding.etTitle.setTextSize(titleSize);
        binding.etContent.setTextSize(contentSize);
    }


    /*MONTAR EL SPINNER DE ESTILO DE FUENTE*/
    private void setupFontStyleSpinner() {
        String[] styles = {"Normal", "Negrita", "Cursiva"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                styles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spFontStyle.setAdapter(adapter);

        binding.spFontStyle.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                        saveFontStyle(position);
                        applyFontStyle(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                }
        );
    }

    /*GUARDADO DEL ESTILO DE FUENTE SELECCIONADO*/
    private void saveFontStyle(int index) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_FONT_STYLE, index)
                .apply();
    }

    /*CARGA EL ESTILO DE FUENTE PREVIAMENTE SELECCIONADO DESDE SharedPreferences*/
    private void loadFontStyle() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int index = prefs.getInt(KEY_FONT_STYLE, 0);
        binding.spFontStyle.setSelection(index);
        applyFontStyle(index);
    }
/*APLICA EL ESTILO DE FUENTE ALMACENADO EN SharedPreferences*/
    private void applyFontStyle(int index) {
        int style;

        switch (index) {
            case 1: // Negrita
                style = Typeface.BOLD;
                break;
            case 2: // Cursiva
                style = Typeface.ITALIC;
                break;
            case 0:
            default:
                style = Typeface.NORMAL;
                break;
        }

        binding.tvHeader.setTypeface(null, style);
        binding.etTitle.setTypeface(null, style);
        binding.etContent.setTypeface(null, style);
    }


}
