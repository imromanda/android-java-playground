package com.example.a2_2_3_wifitools.core;
//→ Importa la clase base para Activities con compatibilidad moderna (AppCompat)
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

//→ Importa el componente Toolbar, que permite crear una barra superior personalizada
import androidx.appcompat.widget.Toolbar;

//→ Importa la clase R, que contiene los recursos del proyecto (layouts, ids, strings, etc.)
import com.example.a2_2_3_wifitools.R;

/**
 * BaseActivity:
 *  - Añade automáticamente la flecha de retroceso
 *  - Configura la toolbar como ActionBar
 *  - Permite reutilizar el mismo comportamiento en todas las Activities
 */
public abstract class BaseActivity extends AppCompatActivity {
//→ Declara una clase abstracta: no se puede instanciar directamente, solo heredarla


    /*protected void setupToolbar(String title) {

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar == null) {
            throw new RuntimeException("❌ Toolbar NO encontrada");
        }

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(title);
        }
    }*/


    protected void setupToolbar(String title) {
    //→ Méetodo protegido para configurar la Toolbar
   // protected: accesible desde clases hijas, Recibe:
    //String title: título que se mostrará en la barra*//*

        Toolbar toolbar = findViewById(R.id.toolbar);
        //→ Busca en el layout la Toolbar con id toolbar

        //→ Comprueba que la Toolbar existe para evitar errores (NullPointerException).
        if (toolbar == null) {
            throw new RuntimeException("❌ Toolbar NO encontrada");
            //→ No hace nada si es null
        }
        //→ Establece la Toolbar como la ActionBar de la Activity
        setSupportActionBar(toolbar);

        // Activamos la flecha de retroceso
            if (getSupportActionBar() != null) {
            //→ Verifica que la ActionBar se haya creado correctamente.
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                //→ Activa el botón de “volver atrás”
                getSupportActionBar().setTitle(title);
                //→ Establece el título que se mostrará en la Toolbar
            }
    }

    @Override
    public boolean onSupportNavigateUp() {
    //  → Sobrescribe el comportamiento del botón de navegación “arriba” (flecha).
        finish(); // Acción al pulsar la flecha
        //→ Cierra la Activity actual y vuelve a la anterior
        return true;
        //→ Indica que la acción de navegación se ha gestionado correctamente
    }
}
