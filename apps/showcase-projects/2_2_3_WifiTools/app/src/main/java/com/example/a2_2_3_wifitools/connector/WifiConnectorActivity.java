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
import com.example.a2_2_3_wifitools.core.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectorActivity extends BaseActivity {

    private WifiManager wifiManager;
    // Gestor del sistema WiFi
    private ListView listWifi;
    // Lista visual de redes disponibles
    private EditText txtPassword;
    // Campo de texto para la contraseña
    private TextView txtStatus;
    // Texto que da información de estado y posibles errores

    private List<ScanResult> scanResults;
    // Lista de los resultados al escanear
    private String selectedSSID = null;
    // SSID seleccionado por el usuario que inicializa como null

  @Override
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Llama al constructor onCreate de la clase padre
        setupToolbar("Wi Fi Direct");
        //Configura la toolbar superior
        setContentView(R.layout.activity_wifi_connector);
      // Carga la interfaz gráfica de la Activity

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
      // Obtiene el servicio WiFi del sistema Android

        listWifi = findViewById(R.id.listWifi);
        // Referencia al ListView de redes Wifi
        txtPassword = findViewById(R.id.txtPassword);
        // Referencia al campo de texto de contraseña
        txtStatus = findViewById(R.id.txtStatus);
        //Referencia al texto de estado
        Button btnScan = findViewById(R.id.btnScan);
        //Referencia al botón de escanear
        Button btnConnect = findViewById(R.id.btnConnect);
        //Referencia al botón de conectar

        btnScan.setOnClickListener(v -> checkPermissionsAndScan());
        //Listener para click en el botón de escanear -> Checkea permisos e inicia el escaneado

        listWifi.setOnItemClickListener((parent, view, position, id) -> {
        // Evento que se ejecuta al seleccionar una red de la lista
            selectedSSID = scanResults.get(position).SSID;
            // Guarda el SSID de la red seleccionada
            txtStatus.setText("Red seleccionada: " + selectedSSID);
            //Muestra la SSID al usuario
        });

        btnConnect.setOnClickListener(v -> {
        //Listener en el botón de conectar
            if (selectedSSID == null) {
                //Si no se ha guardado la SSID (no se ha seleccionado una red todavía)
                txtStatus.setText("Selecciona una red primero");
                //Muestra el mensaje y sale de la función
                return;
            }
            // Verificar la versión de Android. Decide qué API usar según la versión de Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10 (API 29) o superior → Conexion con API moderna, con WifiNetworkSpecifier
                connectToWifiModern(selectedSSID, txtPassword.getText().toString());
            } else {
                // Android 9 o inferior → Conexión con API antigua, con WifiConfiguration
                txtStatus.setText("versión Android 9 o inferior. Se conectará con el método Legacy");
                connectToWifiLegacy(selectedSSID, txtPassword.getText().toString());
                //Muestra el mensaje y ejecuta otro méetodo para conectarse: Legacy
            }
        });
    }

    private void checkPermissionsAndScan() {
  //Méetodo que revisa permisos y empieza el escaneo
        String[] permissions = {
        //Objeto string con los permisos
                Manifest.permission.ACCESS_FINE_LOCATION,
                //Permiso de ubicación
                Manifest.permission.ACCESS_COARSE_LOCATION
                //
        };

        boolean missing = false;
        //Inicializamos la variable missing como falsa

        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                //Si es diferente de GRANTED
                //Missing pasa a ser true = "Faltan permisos"
                missing = true;
            }
        }

        if (missing) {
            //Si missing es true = "Faltan permisos", los pedimos
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            //Si no faltan permisos, escaneamos en busca de wifi
            scanWifi();
        }
    }

    private void scanWifi() {
        try {
        //Intenta
            boolean success = wifiManager.startScan();
            // Inicia el escaneo WiFi
      if (!success) {
            //Si no success = si "Error"
                txtStatus.setText("Error al iniciar escaneo");
                //Mensaje de error y sale de la función
                return;
            }

            scanResults = wifiManager.getScanResults();
            //Trae los resultados del escaneo del wifi manager = scanResults
            List<String> ssids = new ArrayList<>();
            //Crea una nueva ArrayList para las ssids

            for (ScanResult r : scanResults) {
            //Bucle para añadir a la lista cada una de las ssids encontradas
                ssids.add(r.SSID);
            }

      ArrayAdapter<String> adapter =
          new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ssids);
            //Crea un adaptador para mostrar los SSID en el ListView


            listWifi.setAdapter(adapter);
            // Asigna el adaptador al ListView

        } catch (SecurityException e) {
            // Captura errores por falta de permisos
            txtStatus.setText("Permisos insuficientes");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    // Este méetodo solo puede ejecutarse en Android 10 o superior
    private void connectToWifiModern(String ssid, String password)  {
        WifiNetworkSpecifier specifier =
        // Construye la especificación de la red WiFi destino

                new WifiNetworkSpecifier.Builder()
                //Constructor de nuevo objeto
                        .setSsid(ssid)
                        //Define el nombre de la red
                        .setWpa2Passphrase(password)
                        //Define la contraseña WPA2
                        .build();
                        //Construye el objeto final

        NetworkRequest request =
        // Construye la solicitud de conexión de red

                new NetworkRequest.Builder()
                //Constructor de nuevo objeto
                        .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
                        // Indica que queremos una conexión WiFi
                        .setNetworkSpecifier(specifier)
                        // Asocia la red definida anteriormente
                        .build();
                        //Construye la solicitud final

        ConnectivityManager cm =
        // Obtiene el gestor de conectividad del sistema
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        cm.requestNetwork(request, new ConnectivityManager.NetworkCallback() {
        // Solicita la conexión a la red WiFi especificada

            @Override
            public void onAvailable(Network network) {
                // Se ejecuta cuando la conexión se establece correctamente

                runOnUiThread(() -> txtStatus.setText("Conectado a " + ssid));
                // runOnUiThread -> "Ejecuta este código en el hilo principal de la interfaz."
            }

            @Override
            public void onUnavailable() {
                // Se ejecuta cuando la conexión no puede realizarse
                runOnUiThread(() -> txtStatus.setText("No se pudo conectar"));
            }
        });
    }
    private void connectToWifiLegacy(String ssid, String password) {
    // Méetodo reservado para Android 9 o inferior
        txtStatus.setText("Método Legacy pendiente de implementar");
        //Pendiente de implementar
    }


}
