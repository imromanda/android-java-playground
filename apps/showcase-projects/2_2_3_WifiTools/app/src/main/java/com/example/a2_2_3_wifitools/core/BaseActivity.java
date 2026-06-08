package com.example.a2_2_3_wifitools.core;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.example.a2_2_3_wifitools.R;

/**
 * BaseActivity:
 *  - Añade automáticamente la flecha de retroceso
 *  - Configura la toolbar como ActionBar
 *  - Permite reutilizar el mismo comportamiento en todas las Activities
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected void setupToolbar(String title) {

        // Buscamos la toolbar definida en el layout de cada Activity
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            // Activamos la flecha de retroceso
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(title);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Acción al pulsar la flecha
        return true;
    }
}
