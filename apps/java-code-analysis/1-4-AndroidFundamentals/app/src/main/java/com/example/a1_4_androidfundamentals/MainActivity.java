package com.example.a1_4_androidfundamentals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Esta Activity representa la primera pantalla de la app.
// Aquí veremos cómo se crea una Activity y cómo se navega a otra.
public class MainActivity extends AppCompatActivity {

    // Etiqueta para los mensajes de Log (para depuración)
    private static final String TAG = "CicloVidaMain";

    @Override/*Sobreescribe*/
    protected void onCreate(Bundle savedInstanceState) {
        /*protected, solo se puede acceder desde algunos sitios*/
        /*void, el metodo no tiene return, no devuelve nada*/
        /*se ejecuta cuando la Activity se crea por primera vez*/
        /*(Bundle savedInstanceState):
        *Es un parámetro que contiene datos guardados si la
        * Activity se recrea (por ejemplo, al rotar la pantalla).
        * Si no hay datos previos, puede venir como null.
        */
        super.onCreate(savedInstanceState);
        /*Hereda del padre "super" el inicio del metodo onCreate,
        para que haga otras cosas antes de sobreescribir ese metodo con mi codigo*/

        setContentView(R.layout.activity_main);
        /*Asocia esta Activity con su diseño XML (activity_main.xml)*/

        Log.d(TAG, "onCreate()");

        Toast.makeText(this, "MainActivity: onCreate", Toast.LENGTH_SHORT).show();

        // Obtenemos una referencia al botón usando su id del XML
        Button buttonGoToSecond = findViewById(R.id.buttonGoToSecond);

        // Asignamos un "listener" (escuchador) al botón.
        // Un listener es un objeto que "escucha" eventos, como un clic.
        buttonGoToSecond.setOnClickListener(new View.OnClickListener() {
            @Override/*Sobreescribe lo que venga de arriba*/
            public void onClick(View v) {
                // Creamos un Intent para ir desde MainActivity a SecondActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                // Iniciamos la nueva Activity. Android la coloca encima de la back stack.
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        Toast.makeText(this, "MainActivity: onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        Toast.makeText(this, "MainActivity: onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        Toast.makeText(this, "MainActivity: onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        Toast.makeText(this, "MainActivity: onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        Toast.makeText(this, "MainActivity: onDestroy", Toast.LENGTH_SHORT).show();
    }
}

