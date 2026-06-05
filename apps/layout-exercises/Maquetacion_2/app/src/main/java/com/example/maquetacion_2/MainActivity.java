package com.example.maquetacion_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    CheckBox ConditionsRead;
    CheckBox AcceptConditions;
    Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ConditionsRead = findViewById(R.id.ConditionsRead);
        AcceptConditions = findViewById(R.id.AcceptConditions);
        btnAceptar = findViewById(R.id.btnAceptar);

        CompoundButton.OnCheckedChangeListener listener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                        btnAceptar.setEnabled(
                                ConditionsRead.isChecked() && AcceptConditions.isChecked()
                        );
                    }
                };

        ConditionsRead.setOnCheckedChangeListener(listener);
        AcceptConditions.setOnCheckedChangeListener(listener);


// Asignamos el botón del XML a una variable Java usando su ID
        Button botonIrAViewDos = findViewById(R.id.buttonCenter);

// Le ponemos un listener al botón para detectar cuando se pulsa
        botonIrAViewDos.setOnClickListener(

                // Creamos un listener de clics (clase anónima)
                new View.OnClickListener() {

                    // Méetodo que se ejecuta cuando el usuario pulsa el botón
                    @Override
                    public void onClick(View v) {

                        // Creamos un Intent (acción de navegación entre Activities)
                        // MainActivity.this = pantalla actual (origen)
                        // MainActivity2.class = pantalla a la que queremos ir (destino)
                        Intent miIntent = new Intent(
                                MainActivity.this,
                                MainActivity2.class
                        );

                        // Iniciamos la nueva Activity usando el Intent creado
                        startActivity(miIntent);
                    }
                }
        );
    }
}