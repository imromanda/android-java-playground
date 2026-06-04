package com.example.maquetacion_2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    CheckBox condLeidas;
    CheckBox condAcept;
    Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        condLeidas = findViewById(R.id.CondLeidas);
        condAcept = findViewById(R.id.CondAcept);
        btnAceptar = findViewById(R.id.btnAceptar);

        CompoundButton.OnCheckedChangeListener listener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                        btnAceptar.setEnabled(
                                condLeidas.isChecked() && condAcept.isChecked()
                        );
                    }
                };

        condLeidas.setOnCheckedChangeListener(listener);
        condAcept.setOnCheckedChangeListener(listener);
    }
}