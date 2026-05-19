package com.example.a1_4_androidfundamentals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Esta Activity representa la segunda pantalla.
// Desde aquí podemos ir a ThirdActivity o volver a la anterior.
public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "CicloVidaSecond";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.d(TAG, "onCreate()");
        Toast.makeText(this, "SecondActivity: onCreate", Toast.LENGTH_SHORT).show();

        Button buttonGoToThird = findViewById(R.id.buttonGoToThird);
        Button buttonBackFromSecond = findViewById(R.id.buttonBackFromSecond);

        // Ir a ThirdActivity
        buttonGoToThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        // Cerrar esta Activity y volver a la anterior en la back stack
        buttonBackFromSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish() destruye esta Activity y Android vuelve a la anterior
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        Toast.makeText(this, "SecondActivity: onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        Toast.makeText(this, "SecondActivity: onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        Toast.makeText(this, "SecondActivity: onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        Toast.makeText(this, "SecondActivity: onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        Toast.makeText(this, "SecondActivity: onDestroy", Toast.LENGTH_SHORT).show();
    }
}

