package com.example.a2_2_3_wifitools.p2p;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.*;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.a2_2_3_wifitools.R;
import com.example.a2_2_3_wifitools.core.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class WifiP2PDiscoveryActivity extends BaseActivity {

    private WifiP2pManager manager;          // Gestor Wi‑Fi Direct
    private WifiP2pManager.Channel channel;  // Canal de comunicación
    private WifiP2PBroadcastReceiver receiver;

    private IntentFilter intentFilter;

    private ListView listDevices;
    private TextView txtStatus;

    private List<WifiP2pDevice> deviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar("Wi Fi Direct");
        setContentView(R.layout.activity_wifi_p2p_discovery);

        // Inicializamos el manager y el canal
        manager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        listDevices = findViewById(R.id.listDevices);
        txtStatus = findViewById(R.id.txtStatus);

        Button btnDiscover = findViewById(R.id.btnDiscover);

        // Filtro de eventos que queremos escuchar
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Botón para iniciar la búsqueda de dispositivos
        btnDiscover.setOnClickListener(v -> checkPermissionsAndDiscover());
    }

    // Comprueba permisos antes de iniciar discovery
    private void checkPermissionsAndDiscover() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            discoverPeers();
        }
    }

    // Inicia la búsqueda de dispositivos P2P
    private void discoverPeers() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            txtStatus.setText("Permiso de ubicación no concedido");
            return;
        }

        txtStatus.setText("Buscando dispositivos...");

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                txtStatus.setText("Discovery iniciado");
            }

            @Override
            public void onFailure(int reason) {
                txtStatus.setText("Error al iniciar discovery: " + reason);
            }
        });
    }

    // Actualiza la lista de dispositivos detectados
    public void updateDeviceList(Iterable<WifiP2pDevice> devices) {

        deviceList.clear();
        List<String> names = new ArrayList<>();

        for (WifiP2pDevice d : devices) {
            deviceList.add(d);
            names.add(d.deviceName + " (" + d.deviceAddress + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                names
        );

        listDevices.setAdapter(adapter);
        txtStatus.setText("Dispositivos encontrados: " + deviceList.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new WifiP2PBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
