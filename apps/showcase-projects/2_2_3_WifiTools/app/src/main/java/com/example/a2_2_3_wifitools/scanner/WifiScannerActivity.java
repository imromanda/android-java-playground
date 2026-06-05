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

import java.util.List;

public class WifiScannerActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private TextView txtResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scanner);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        txtResults = findViewById(R.id.txtResults);

        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(v -> checkPermissionsAndScan());
    }

    private void checkPermissionsAndScan() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean missing = false;

        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED) {
                missing = true;
            }
        }

        if (missing) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            scanWifi();
        }
    }

    private void scanWifi() {
        try {
            boolean success = wifiManager.startScan();

            if (!success) {
                txtResults.setText("Error al iniciar el escaneo");
                return;
            }

            List<ScanResult> results = wifiManager.getScanResults();
            showResults(results);

        } catch (SecurityException e) {
            txtResults.setText("Permisos insuficientes para escanear WiFi");
        }
    }

    private void showResults(List<ScanResult> results) {
        StringBuilder builder = new StringBuilder();

        for (ScanResult r : results) {
            builder.append("SSID: ").append(r.SSID).append("\n");
            builder.append("RSSI: ").append(r.level).append(" dBm\n");
            builder.append("Frecuencia: ").append(r.frequency).append(" MHz\n");
            builder.append("Seguridad: ").append(r.capabilities).append("\n");
            builder.append("------------------------\n");
        }

        txtResults.setText(builder.toString());
    }
}