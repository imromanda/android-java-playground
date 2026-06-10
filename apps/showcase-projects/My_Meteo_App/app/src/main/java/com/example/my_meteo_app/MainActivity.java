package com.example.my_meteo_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnHttpUrlConnection = findViewById(R.id.btnHttpURLConnection);
        //Obtiene referencia al primer botón
        btnHttpUrlConnection.setOnClickListener( view -> {
            startActivity(new Intent(this, HttpUrlConnectionActivity.class));
        });

        Button btnOkHttp = findViewById(R.id.btnOkHttp);
        //Obtiene referencia al primer botón
        btnOkHttp.setOnClickListener( view -> {
            startActivity(new Intent(this, OkHttpActivity.class));
        });


        Button btnHttpsSecurity = findViewById(R.id.btnHttpsSecurity);
        //Obtiene referencia al primer botón
        btnHttpsSecurity.setOnClickListener( view -> {
            startActivity(new Intent(this, HttpsSecurityActivity.class));
        });


        Button btnEfficiency = findViewById(R.id.btnEfficiency);
        //Obtiene referencia al primer botón
        btnEfficiency.setOnClickListener( view -> {
            startActivity(new Intent(this, HttpEfficiencyActivity.class));
        });


        Button btnWeatherIcons = findViewById(R.id.btnWeatherIcons);
        //Obtiene referencia al primer botón
        btnWeatherIcons.setOnClickListener( view -> {
            startActivity(new Intent(this, WeatherIconsActivity.class));
        });

    }
}