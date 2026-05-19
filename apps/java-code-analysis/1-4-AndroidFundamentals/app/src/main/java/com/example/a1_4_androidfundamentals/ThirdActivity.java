package com.example.a1_4_androidfundamentals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Esta Activity representa la tercera pantalla.
// Desde aquí solo volvemos hacia atrás.
public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "CicloVidaThird";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Log.d(TAG, "onCreate()");
        Toast.makeText(this, "ThirdActivity: onCreate", Toast.LENGTH_SHORT).show();

        Button buttonBackFromThird = findViewById(R.id.buttonBackFromThird);

        buttonBackFromThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra esta Activity y vuelve a la anterior en la pila
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        Toast.makeText(this, "ThirdActivity: onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        Toast.makeText(this, "ThirdActivity: onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        Toast.makeText(this, "ThirdActivity: onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        Toast.makeText(this, "ThirdActivity: onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        Toast.makeText(this, "ThirdActivity: onDestroy", Toast.LENGTH_SHORT).show();
    }
}

