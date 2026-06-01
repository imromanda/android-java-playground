package com.example.sensores_8_1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor lightSensor;
    private Sensor proximitySensor;

    private TextView tvSteps, tvGyro, tvLight, tvProximity, tvAccuracy;
    private int stepCount = 0;

    // Umbral simple para “detectar pasos”
    private static final float STEP_THRESHOLD = 12f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1) Referencias UI
        tvSteps = findViewById(R.id.tvSteps);
        tvGyro = findViewById(R.id.tvGyro);
        tvLight = findViewById(R.id.tvLight);
        tvProximity = findViewById(R.id.tvProximity);
        tvAccuracy = findViewById(R.id.tvAccuracy);

        // 2) SensorManager y sensores (Sección A)
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 3) Registro de sensores con frecuencia adecuada (Sección C)
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor,
                    SensorManager.SENSOR_DELAY_UI);
        }
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 4) Desregistrar sensores para ahorrar batería (Sección D)
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                handleAccelerometer(event);
                break;

            case Sensor.TYPE_GYROSCOPE:
                handleGyroscope(event);
                break;

            case Sensor.TYPE_LIGHT:
                handleLight(event);
                break;

            case Sensor.TYPE_PROXIMITY:
                handleProximity(event);
                break;
        }

    }

    //   1.8.4.1. Acelerómetro → Podómetro simple (Sección E)

    private void handleAccelerometer(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Magnitud del vector aceleración
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);

        if (magnitude > STEP_THRESHOLD) {
            stepCount++;
            tvSteps.setText("Pasos: " + stepCount);
        }
    }

    // 1.8.4.2. Giroscopio → “mini RA” textual (Sección F)

    private void handleGyroscope(SensorEvent event) {
        float rx = event.values[0];
        float ry = event.values[1];
        float rz = event.values[2];

        String text = String.format("Giroscopio\nX: %.2f\nY: %.2f\nZ: %.2f", rx, ry, rz);
        tvGyro.setText(text);
    }

    // 1.8.4.3. Luz → “modo brillo” (Sección G)

    private void handleLight(SensorEvent event) {
        float lightLevel = event.values[0];

        String mode;
        if (lightLevel < 10) {
            mode = "Oscuro (bajar brillo)";
        } else if (lightLevel < 1000) {
            mode = "Interior";
        } else {
            mode = "Exterior (subir brillo)";
        }

        String text = String.format("Luz: %.2f lx\nModo: %s", lightLevel, mode);
        tvLight.setText(text);
    }

    //  1.8.4.4. Proximidad → Cerca / Lejos (Sección H)

    private void handleProximity(SensorEvent event) {
        float distance = event.values[0];
        float maxRange = event.sensor.getMaximumRange();

        String state = (distance < maxRange) ? "Cerca" : "Lejos";
        String text = String.format("Proximidad: %.2f\nEstado: %s", distance, state);
        tvProximity.setText(text);
    }

    // 1.8.5. 3. onAccuracyChanged() bien usado (Sección B)

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        String sensorName = sensor.getName();
        String accText;

        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                accText = "Alta";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                accText = "Media";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                accText = "Baja";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
            default:
                accText = "No fiable";
                break;
        }

        tvAccuracy.setText("Precisión " + sensorName + ": " + accText);
    }

}

