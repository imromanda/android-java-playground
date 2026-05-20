package com.example.ui_demo_accessible;

import android.os.Bundle;
import android.view.View;

import com.example.ui_demo_accessible.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; // ViewBinding para activity_main.xml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflamos el layout usando ViewBinding
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    //Listener moderno con lambda
        binding.button1.setOnClickListener(v -> {
            //Acción al presionar el botón: Cambiar el texto
            binding.textTitle.setText("¡Has pulstado el boton!");




        });

        //Listener segundo botón con lambda
        binding.button2.setOnClickListener(v -> {
            //Acción al presionar el botón: Mostrar el FrameLayout
            binding.frameContent.setVisibility(View.VISIBLE);

        });

        //Listener moderno con lambda
        binding.button3.setOnClickListener(v -> {
            //Acción al presionar el botón: Vuelve al estado inicial
            binding.textTitle.setText("Demo de Interfaz");
            binding.frameContent.setVisibility(View.GONE);


        });

    }
}
