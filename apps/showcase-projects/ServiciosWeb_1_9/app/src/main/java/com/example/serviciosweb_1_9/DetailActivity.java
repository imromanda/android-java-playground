package com.example.serviciosweb_1_9;

// Permite recibir datos enviados desde otra Activity
import android.content.Intent;

// Permite recuperar el estado guardado de la Activity
import android.os.Bundle;

// Componente visual para mostrar texto
import android.widget.TextView;

// Clase base para Activities compatibles con Android moderno
import androidx.appcompat.app.AppCompatActivity;

// Activity que muestra los detalles de un usuario seleccionado
public class DetailActivity extends AppCompatActivity {

    // Meetodo que se ejecuta al abrir esta pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Llama al Meetodo de la clase padre
        super.onCreate(savedInstanceState);

        // Carga el layout activity_detail.xml
        setContentView(R.layout.activity_detail);

        // Activa la flecha de volver atrás en la barra superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtiene la referencia al TextView del nombre
        TextView tvDetailName = findViewById(R.id.tvDetailName);

        // Obtiene la referencia al TextView del email
        TextView tvDetailEmail = findViewById(R.id.tvDetailEmail);

        // Obtiene la referencia al TextView del ID
        TextView tvDetailId = findViewById(R.id.tvDetailId);

        // Recupera el Intent que abrió esta Activity
        Intent intent = getIntent();

        // Obtiene el nombre enviado desde MainActivity
        String name = intent.getStringExtra("name");

        // Obtiene el email enviado desde MainActivity
        String email = intent.getStringExtra("email");

        // Obtiene el ID enviado desde MainActivity
        // Si no existe, devuelve -1 por defecto
        int id = intent.getIntExtra("id", -1);

        // Muestra el nombre en pantalla
        tvDetailName.setText(name);

        // Muestra el email en pantalla
        tvDetailEmail.setText("Email: " + email);

        // Muestra el ID en pantalla
        tvDetailId.setText("ID: " + id);
    }

    // Se ejecuta cuando el usuario pulsa la flecha de volver
    @Override
    public boolean onSupportNavigateUp() {

        // Cierra la Activity actual y vuelve a MainActivity
        finish();

        // Indica que el evento ha sido gestionado correctamente
        return true;
    }
}