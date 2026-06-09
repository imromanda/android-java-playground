package com.example.a2_2_3_wifitools.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.a2_2_3_wifitools.R;
import com.example.a2_2_3_wifitools.core.BaseActivity;

import java.util.List;

public class WifiScannerActivity extends BaseActivity {
// Activity encargada de escanear redes WiFi
//Hereda de BaseActivity

    private WifiManager wifiManager;
    // Objeto encargado de controlar las funciones WiFi
    private TextView txtResults;
    // TextView donde se mostrarán los resultados del escaneo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Llama al méetodo onCreate de la clase padre

        setContentView(R.layout.activity_wifi_scanner);
        //Localiza el layout correspondiente para esta Actividad

        setupToolbar("Wifi Scanner Activity");
        // Configura la barra superior con un título

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        // Obtiene una referencia al servicio WiFi del sistema Android

        txtResults = findViewById(R.id.txtResults);
        //Vincula el TextView del layout con la variable txtResults

    Button btnScan = findViewById(R.id.btnScan);
    // Obtiene una referencia al botón de escaneo

        btnScan.setOnClickListener(v -> checkPermissionsAndScan());
        // Define qué ocurre cuando el usuario pulsa el botón
    }

    private void checkPermissionsAndScan() {
    // Comprueba que los permisos necesarios estén concedidos

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        //Un array de strings "permissions"
        //En el que están los permisos de localización

        boolean missing = false;
        //Variable para saber si falta algún permiso

        for (String p : permissions) {
            // Recorre todos los permisos requeridos
            if (ContextCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED) {
                missing = true;
            }
        }
        // Si falta algún permiso...
        if (missing) {
            ActivityCompat.requestPermissions(this, permissions, 1);
            //pídelo
        } else {
            //Si no, escanea wifi
            scanWifi();
        }
    }

    private void scanWifi() {
        try {
        // Inicia el escaneo WiFi
            boolean success = wifiManager.startScan();

            if (!success) {
            // Si el escaneo no pudo iniciarse...
                txtResults.setText("Error al iniciar el escaneo");
                //Devuelve este texto de error y sal de la función
                return;
            }
            List<ScanResult> results = wifiManager.getScanResults();
            // Obtiene la lista de redes encontradas

            showResults(results);
            // Muestra los resultados en pantalla

        } catch (SecurityException e) {
            // Captura errores de permisos insuficientes
            txtResults.setText("Permisos insuficientes para escanear WiFi");
            //Devuelve este texto de error

        }
    }

    private void showResults(List<ScanResult> results) {
    //Función para mostrar Resultados, como parámetros la lista de resultados de scanResult
        StringBuilder builder = new StringBuilder();
        // StringBuilder para construir el texto eficientemente

        for (ScanResult r : results) {
        //Para cada resultado...
            builder.append("SSID: ").append(r.SSID).append("\n");
            // Añade el nombre de la red (SSID)
            builder.append("RSSI: ").append(r.level).append(" dBm\n");
            // Añade la potencia de la señal en dBm
            builder.append("Frecuencia: ").append(r.frequency).append(" MHz\n");
            // Añade la frecuencia de trabajo
            builder.append("Seguridad: ").append(r.capabilities).append("\n");
            // Añade el tipo de seguridad de la red
      builder.append("------------------------\n");
            // Línea separadora entre redes

        }

        txtResults.setText(builder.toString());
        // Muestra toodo el texto generado en el TextView

    }
}