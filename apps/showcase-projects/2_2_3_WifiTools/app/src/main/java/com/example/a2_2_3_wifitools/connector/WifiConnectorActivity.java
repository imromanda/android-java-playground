package com.example.a2_2_3_wifitools.connector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.a2_2_3_wifitools.R;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectorActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listWifi;
    private EditText txtPassword;
    private TextView txtStatus;

    private List<ScanResult> scanResults;
    private String selectedSSID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connector);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        listWifi = findViewById(R.id.listWifi);
        txtPassword = findViewById(R.id.txtPassword);
        txtStatus = findViewById(R.id.txtStatus);

        Button btnScan = findViewById(R.id.btnScan);
        Button btnConnect = findViewById(R.id.btnConnect);

        btnScan.setOnClickListener(v -> checkPermissionsAndScan());

        listWifi.setOnItemClickListener((parent, view, position, id) -> {
            selectedSSID = scanResults.get(position).SSID;
            txtStatus.setText("Red seleccionada: " + selectedSSID);
        });

        btnConnect.setOnClickListener(v -> {
            if (selectedSSID == null) {
                txtStatus.setText("Selecciona una red primero");
                return;
            }
            connectToWifi(selectedSSID, txtPassword.getText().toString());
        });
    }

    private void checkPermissionsAndScan() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean missing = false;
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
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
                txtStatus.setText("Error al iniciar escaneo");
                return;
            }

            scanResults = wifiManager.getScanResults();
            List<String> ssids = new ArrayList<>();

            for (ScanResult r : scanResults) {
                ssids.add(r.SSID);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    ssids
            );

            listWifi.setAdapter(adapter);

        } catch (SecurityException e) {
            txtStatus.setText("Permisos insuficientes");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void connectToWifi(String ssid, String password) {

        WifiNetworkSpecifier specifier =
                new WifiNetworkSpecifier.Builder()
                        .setSsid(ssid)
                        .setWpa2Passphrase(password)
                        .build();

        NetworkRequest request =
                new NetworkRequest.Builder()
                        .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
                        .setNetworkSpecifier(specifier)
                        .build();

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        cm.requestNetwork(request, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                runOnUiThread(() -> txtStatus.setText("Conectado a " + ssid));
            }

            @Override
            public void onUnavailable() {
                runOnUiThread(() -> txtStatus.setText("No se pudo conectar"));
            }
        });
    }
}
