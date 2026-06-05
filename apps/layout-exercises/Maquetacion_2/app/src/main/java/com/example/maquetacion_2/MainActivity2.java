package com.example.maquetacion_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main2);
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });




// Asignamos el botón del XML a una variable Java usando su ID
      Button botonIrAViewUno = findViewById(R.id.buttonBack);

// Le ponemos un listener al botón para detectar cuando se pulsa
      botonIrAViewUno.setOnClickListener(

              // Creamos un listener de clics (clase anónima)
              new View.OnClickListener() {

                  // Méetodo que se ejecuta cuando el usuario pulsa el botón
                  @Override
                  public void onClick(View v) {

                      // Creamos un Intent (acción de navegación entre Activities)
                      // MainActivity.this = pantalla actual (origen)
                      // MainActivity2.class = pantalla a la que queremos ir (destino)
                      Intent miIntent = new Intent(
                              MainActivity2.this,
                              MainActivity.class
                      );

                      // Iniciamos la nueva Activity usando el Intent creado
                      startActivity(miIntent);
                  }
              }
      );

  }
}
