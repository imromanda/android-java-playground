package com.example.a2_2_3_wifitools.p2p;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class WifiP2PDiscoveryActivity extends BaseActivity {
// Activity encargada del descubrimiento y conexión WiFi Direct

    private WifiP2pManager manager;
    // Gestor principal de WiFi Direct
    private WifiP2pManager.Channel channel;
    // Canal de comunicación con el framework WiFi Direct
    private WifiP2PBroadcastReceiver receiver;
    // BroadcastReceiver que escuchará eventos WiFi Direct

    private IntentFilter intentFilter;
    // Filtro de eventos que queremos recibir

    private ListView listDevices;
    //Lista de dispositivos

    private TextView txtStatus;
    // Campo donde se muestran estados y mensajes

    private List<WifiP2pDevice> deviceList = new ArrayList<>();
    // Lista interna de dispositivos detectados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Llama al méetodo onCreate del padre

        setContentView(R.layout.activity_wifi_p2p_discovery);
        // Lista interna de dispositivos detectados

        setupToolbar("Wifi Discovery");
        //Configura la barra de navegación con Título

        Button btnConnect = findViewById(R.id.btnConnect);
        //Identifica el botón de Conectar
        // Obtiene referencia al botón Conectar

        btnConnect.setOnClickListener(v -> {
        //Define el comportamiento del botón
            if (selectedDevice == null) {
                //Si no hay ningún dispositivo seleccionado
                txtStatus.setText("Selecciona un dispositivo primero");
                //Muestra mensaje y sale de la función
                return;
            }
            connectToSelectedDevice();
            //Si sí que hay un dispositivo seleccionado ejecuta la función connectToSelectedDevice
        });


        // Inicializamos el manager y el canal

        manager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        // Obtiene el servicio WiFi Direct del sistema

        channel = manager.initialize(this, getMainLooper(), null);
        // Inicializa el canal de comunicación

        listDevices = findViewById(R.id.listDevices);
        //Obtiene la referencia el componente listDevices

        listDevices.setOnItemClickListener((parent, view, position, id) -> {
        // Evento que ocurre al seleccionar un dispositivo de la lista

            // Obtenemos el dispositivo seleccionado con su posición
            WifiP2pDevice device = deviceList.get(position);

            // Mostramos información básica al usuario
            txtStatus.setText("Dispositivo seleccionado:\n" +
                    device.deviceName + "\n" +
                    device.deviceAddress);

            // Guardamos el dispositivo seleccionado para usarlo más adelante
            selectedDevice = device;
        });


        txtStatus = findViewById(R.id.txtStatus);
        // Referencia al TextView de estado

        Button btnDiscover = findViewById(R.id.btnDiscover);
        //Referencia al botón btnDiscover

        // Filtro de eventos que queremos escuchar
        intentFilter = new IntentFilter();

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Evento cuando cambia la lista de dispositivos

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Evento cuando cambia el estado de la conexión

        btnDiscover.setOnClickListener(v -> checkPermissionsAndDiscover());
        // Botón para iniciar la búsqueda de dispositivos
    }

    private void checkPermissionsAndDiscover() {
        // Comprueba permisos antes de iniciar discovery

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Comprueba si existe permiso de ubicación,
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
      //  Solicita permiso al usuario

    } else {
            discoverPeers();
            //Si sí que está el permiso ejecuta discoverPeers()
        }
    }

    private void discoverPeers() {
    // Inicia la búsqueda de dispositivos P2P
        //Comprueba si el permiso es distinto de permiso concedido
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            txtStatus.setText("Permiso de ubicación no concedido");
            return;
            //Si el permiso no está concedido muestra el mensaje y sale de la función
        }

        txtStatus.setText("Buscando dispositivos...");
        //Cambia el texto a Buscando dispositivos

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
        // Inicia el proceso de descubrimiento

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
    private void connectToSelectedDevice() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Comprueba permisos
            txtStatus.setText("Permiso de ubicación no concedido");
            return;
        }

        WifiP2pConfig config = new WifiP2pConfig();
        // Configuración de conexión
        config.deviceAddress = selectedDevice.deviceAddress;
        // Dirección MAC del dispositivo destino

        txtStatus.setText("Conectando a " + selectedDevice.deviceName + "...");
        //Actualiza el texto de estado

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            // Inicia la conexión

            @Override
            public void onSuccess() {
                txtStatus.setText("Conexión iniciada. Esperando confirmación...");
            }

            @Override
            public void onFailure(int reason) {
                txtStatus.setText("Error al conectar: " + reason);
            }
        });
    }
    private void goToTransferActivity(boolean isGroupOwner, String ownerIp) {
        Intent intent = new Intent(this, WifiP2PTransferActivity.class);
        // Crea un Intent para abrir la Activity de transferencia
        intent.putExtra("isGroupOwner", isGroupOwner);
        // Envía si somos GO o Cliente

        intent.putExtra("ownerIp", ownerIp);
        // Envía la IP del GO

        startActivity(intent);
        // Inicia la nueva Activity

    }


    // Actualiza la lista de dispositivos detectados
    public void updateDeviceList(Iterable<WifiP2pDevice> devices) {
    //Actualiza la lista de dispositivos
        deviceList.clear();
        //Borra la lista antigua
        List<String> names = new ArrayList<>();
        //Crea un nuevo array

        for (WifiP2pDevice d : devices) {
            deviceList.add(d);
            // Guarda el objeto completo
            names.add(d.deviceName + " (" + d.deviceAddress + ")");
            // Crea texto visible para el ListView
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
        // Adaptador para mostrar los nombres
                this,
                android.R.layout.simple_list_item_1,
                names
        );

        listDevices.setAdapter(adapter);
         // Actualiza el ListView
    txtStatus.setText("Dispositivos encontrados: " + deviceList.size());
    // Muestra cantidad encontrada
  }

    private WifiP2pDevice selectedDevice = null;
    // Iguala a null el dispositivo actualmente seleccionado

//Se ejecuta cuando la app vuelve al primer plano
    @Override
    protected void onResume() {
        super.onResume();
        receiver = new WifiP2PBroadcastReceiver(manager, channel, this);
        // Crea el BroadcastReceiver
        registerReceiver(receiver, intentFilter);
        // Lo registra en el sistema

    }
//Se ejecuta cuando la app sale del primer plano
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        // Desregistra el receiver
    }
    //Cuando somos el Group Owner
    public void onGroupOwnerReady() {
        txtStatus.setText("Conectado como Group Owner");
        goToTransferActivity(true, null);
    }

    //Cuando somos el Client
    public void onClientReady(InetAddress ownerAddress) {
        txtStatus.setText("Conectado como Cliente");
        goToTransferActivity(false, ownerAddress.getHostAddress());
    }
}
