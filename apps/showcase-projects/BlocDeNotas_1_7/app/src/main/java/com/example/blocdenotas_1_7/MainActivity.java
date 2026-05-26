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
    } //AQUÍ ACABA EL METODO ONCREATE

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
        //Aquí se empiezan a escuchar los eventos en el spinner
         /*set	configurar
        OnItemSelected	cuando se selecciona un elemento
        Listener	escuchador/evento*/

        //DENTRO DEL .setOnItemSelectedListener va toodo esto:
            new AdapterView.OnItemSelectedListener() {
            //
                @Override
                public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                    // Guardar el perfil seleccionado en SharedPreferences
                    saveAccessibilityProfile(position);
                    applyFontSizeProfile(position); // NUEVO: aplicar tamaño de fuente
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Nada
                }
                }
        );
    } //FIN DE setupAccessibilitySpinner



    /* CÓMO SE VA A GUARDAR EL PERFIL DE ACCESIBILIDAD SELECCIONADO en SharedPreferences. */
    private void saveAccessibilityProfile(int index) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_PROFILE, index)
                .apply();
    }


/*CARGA DEL PERFIL DE ACCESIBILIDAD */
    private void loadAccessibilityProfile() {
        // Carga el tamaño de texto guardado previamente

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int index = prefs.getInt(KEY_PROFILE, 0); // 0 = Normal por defecto
        binding.spAccessibilityProfile.setSelection(index);
        applyFontSizeProfile(index); // NUEVO: aplicar tamaño al iniciar
    }


    /* APLICAR EL TAMAÑO DE FUENTE */
    private void applyFontSizeProfile(int index) {
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
