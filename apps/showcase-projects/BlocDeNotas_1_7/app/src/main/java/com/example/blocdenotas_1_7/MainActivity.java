package com.example.blocdenotas_1_7;import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.content.SharedPreferences;

import com.example.blocdenotas_1_7.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // ViewBinding

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AccesibilidadPrefs";
    private static final String KEY_PROFILE = "accessibility_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflar el layout con ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAccessibilitySpinner();
        loadAccessibilityProfile();
    }

    /** Configura el Spinner de perfiles de accesibilidad. */
    private void setupAccessibilitySpinner() {
        // Opciones de perfil de accesibilidad
        String[] profiles = {"Normal", "Grande", "Muy grande"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                profiles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spAccessibilityProfile.setAdapter(adapter);

        binding.spAccessibilityProfile.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                        // Guardar el perfil seleccionado en SharedPreferences
                        saveAccessibilityProfile(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Nada
                    }
                }
        );
    }

    /** Guarda el perfil de accesibilidad seleccionado en SharedPreferences. */
    private void saveAccessibilityProfile(int index) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_PROFILE, index)
                .apply();
    }

    private void loadAccessibilityProfile() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int index = prefs.getInt(KEY_PROFILE, 0); // 0 = Normal por defecto
        binding.spAccessibilityProfile.setSelection(index);








    }
}

