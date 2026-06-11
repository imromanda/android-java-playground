package com.example.my_meteo_app;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class HttpEfficiencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_efficiency_http);

        Button btnBack = findViewById(R.id.btnBack);
        //Obtiene referencia al primer botón
        btnBack.setOnClickListener( view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }

}
