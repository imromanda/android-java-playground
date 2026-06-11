package com.example.my_meteo_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WeatherIconsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons_activity);


        Button btnBack = findViewById(R.id.btnBack);
        //Obtiene referencia al primer botón
        btnBack.setOnClickListener( view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        ImageView imgIcon = findViewById(R.id.imgIcon);
        TextView txtTemperature = findViewById(R.id.txtTemperature);
        TextView txtWindSpeed = findViewById(R.id.txtWindSpeed);
        TextView txtEstado = findViewById(R.id.txtEstado);

    }


}
