package com.example.a2_2_3_wifitools;

import com.example.a2_2_3_wifitools.core.BaseActivity;
import com.example.a2_2_3_wifitools.scanner.WifiScannerActivity;
import com.example.a2_2_3_wifitools.connector.WifiConnectorActivity;
import com.example.a2_2_3_wifitools.p2p.WifiP2PDiscoveryActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Méetodo llamado al crear la actividad.
        // savedInstanceState contiene el estado previamente guardado de la actividad, si existe.
        super.onCreate(savedInstanceState);

        // Establece el layout que se mostrará como interfaz de usuario de esta actividad.
        setContentView(R.layout.activity_main);

        // Configura la barra de herramientas con el título indicado.
        setupToolbar("Main Activity");

        // Obtiene una referencia al botón que abre la actividad de escaneo wifi.
        Button btnScanner = findViewById(R.id.btnScanner);

        // Obtiene una referencia al botón que abre la actividad de conexión wifi.
        Button btnConnector = findViewById(R.id.btnConnector);

        // Obtiene una referencia al botón que abre la actividad de descubrimiento wifi Direct.
        Button btnP2P = findViewById(R.id.btnP2P);

        // Asigna un listener que inicia la actividad de escaneo al pulsar el botón.
        btnScanner.setOnClickListener(v ->
                startActivity(new Intent(this, WifiScannerActivity.class)));

        // Asigna un listener que inicia la actividad de conexión al pulsar el botón.
        btnConnector.setOnClickListener(v ->
                startActivity(new Intent(this, WifiConnectorActivity.class)));

        // Asigna un listener que inicia la actividad de descubrimiento wifi Direct al pulsar el botón.
        btnP2P.setOnClickListener(v ->
                startActivity(new Intent(this, WifiP2PDiscoveryActivity.class)));
    }
}