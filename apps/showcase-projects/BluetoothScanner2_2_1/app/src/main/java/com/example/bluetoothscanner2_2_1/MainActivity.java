package com.example.bluetoothscanner2_2_1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//Permiten lanzar actividades y recibir resultados usando la API moderna de Android
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

//Lo recibido no puede ser Null
import androidx.annotation.NonNull;

//Utilidades para gestionar permisos y compatibilidad
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
// BluetoothAdapter → controla el Bluetooth del dispositivo
// BluetoothDevice → representa un dispositivo Bluetooth encontrado
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

//Permiten recibir eventos del sistema.
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import android.content.pm.PackageManager;
import android.os.Build;

//Elementos de interfaz
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//Colección de lista dinámica para almacenar dispositivos encontrados
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//CONSTANTES - Identificadores para distinguir solicitudes
  //No se usa porque se ha migrado al sistema moderno con ActivityResultLauncher
    // private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSIONS = 2;

    // VARIABLES PRINCIPALES

    private BluetoothAdapter bluetoothAdapter;
    //Controlador del Bluetooth

    private Button btnEnableBt;
    //Botón para encender el bluetooth

    private Button btnScan;
    //Botón para iniciar el escaneado de dipositivos

    private TextView tvStatus;
    //TextView para el estado

    private ListView listDevices;
    //Lista visual para los dispositivos encontrados

    private ArrayAdapter<String> deviceListAdapter;
    //Adaptador que conecta la lista de datos con la interfaz

    private final ArrayList<String> devices = new ArrayList<>();
    //Lista donde se almacenan los nombres de los dispositivos y sus IPS
    private final BroadcastReceiver btReceiver = new BroadcastReceiver() {
    //Escucha eventos Bluetooth enviados por Android

        @Override
        public void onReceive(Context context, Intent intent) {
            //Se ejecuta automáticamente cuando llega un evento

            String action = intent.getAction();
            //Obtener acción recibida

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //Se ejecuta cada vez que aparece un dispositivo nuevo

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //Extrae el dispositivo encontrado

                if (device != null) {
                // Comprobamos que se ha recibido un dispositivo válido
                    String name;
                    // Declaramos una variable String para almacenar el nombre del dispositivo
                    try {
                        // Intentamos obtener el nombre del dispositivo
                        name = device.getName();

                    } catch (SecurityException e) {
                        // Si no tenemos permisos para acceder al nombre del dispositivo
                        name = "Nombre no disponible (sin permiso)";
                    }

                    if (name == null || name.isEmpty()) {
                        // Si el dispositivo no tiene nombre o el nombre está vacío
                        name = "Dispositivo desconocido";
                        //Entonces nombre será "dispositivo desconocido
                    }

                    String address = device.getAddress();
                    // Obtenemos la dirección MAC del dispositivo
                    String item = name + "\n" + address;
                    // Construimos el texto que se mostrará en la lista:
                    // nombre del dispositivo + salto de línea + dirección MAC

                    if (!devices.contains(item)) {
                        // Si el dispositivo aún no está en la lista
                        devices.add(item);
                        //añade ese dispositivo a la lista devices
                        deviceListAdapter.notifyDataSetChanged();
                        // Avisamos al adaptador para que actualice la ListView
                    }
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //Cuando empieza la búsqueda
                tvStatus.setText("Escaneando dispositivos...");

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Cuando la búsqueda termina
                tvStatus.setText("Escaneo finalizado");
            }
        }
    };

    private final ActivityResultLauncher<Intent> enableBtLauncher =
    //Sistema moderno para recibir resultados.
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                           //Si el usuario acepta que la app encienda el bluetooth manda el toast
                            Toast.makeText(this, "Bluetooth activado", Toast.LENGTH_SHORT).show();
                        } else {
                            //Si no acepta, manda este otro toast
                            Toast.makeText(this, "No se activó Bluetooth", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RELACIONAMOS LAS VARIABLES CON LOS ELEMENTOS DE LA VISTA
        //Conecta variables Java con botones y vistas.
        btnEnableBt = findViewById(R.id.btnEnableBt);
        btnScan = findViewById(R.id.btnScan);
        tvStatus = findViewById(R.id.tvStatus);
        listDevices = findViewById(R.id.listDevices);

        //Obtiene el adaptador bluetooth del teléfono
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
         // Comprobar soporte Bluetooth


        if (bluetoothAdapter == null) {
            //Si el dispositivo no tiene bluetooth
            Toast.makeText(this, "Este dispositivo no soporta Bluetooth", Toast.LENGTH_LONG).show();
            btnEnableBt.setEnabled(false);
            btnScan.setEnabled(false);
            return;
            //no se habilita ningún botón y sale de la función

        }

        deviceListAdapter = new ArrayAdapter<>(
                //Crear adaptador de lista, le pasa el contexto, la vista y la lista de dispositivos
                this,
                android.R.layout.simple_list_item_1,
                devices         );
        //Activa la conexión
        listDevices.setAdapter(deviceListAdapter);

        registerBtReceiver();
        //Empieza a escuchar eventos Bluetooth.
        checkAndRequestPermissions();
        //Comprueba permisos necesarios

        btnEnableBt.setOnClickListener(v -> {
            // Comprobar permiso BLUETOOTH_CONNECT (Android 12+)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                            REQUEST_PERMISSIONS
                    );
                    return;
                }
            }

            if (!bluetoothAdapter.isEnabled()) {
                //Si el bluetooth no está activado
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // Al estar deprecado, sustituimos la versión antigua...
                // startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                // por este launcher que hemos creado anteriormente
                enableBtLauncher.launch(enableBtIntent);
            } else if (bluetoothAdapter.isEnabled()) {
                //Si el bluetooth está activado suelta el toast
                Toast.makeText(this, "Bluetooth ya está activado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth ya está activado", Toast.LENGTH_SHORT).show();
            }
        });
        //Misma lógica para el botón de escanear
        btnScan.setOnClickListener(v -> {
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "Activa Bluetooth primero", Toast.LENGTH_SHORT).show();
                return;
            }

            if (bluetoothAdapter.isDiscovering()) {
                //Si ya está buscando, cancela la búsqueda previa
                bluetoothAdapter.cancelDiscovery();
            }

            devices.clear();
            //borra la lista dispositivos
            deviceListAdapter.notifyDataSetChanged();
            //Notifica que se cambió el set de datos

            boolean started = bluetoothAdapter.startDiscovery();
            //Empieza el escaneo

            // Si no se pudo iniciar el escaneo, mostramos un mensaje
            if (!started) {
                tvStatus.setText("No se pudo iniciar el escaneo");
            }
        });
    }

    private void registerBtReceiver() {
        //Crea un filtro de eventos
        // Creamos un filtro para el receptor
        IntentFilter filter = new IntentFilter();
        // El receptor escuchará tres eventos:
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        // Registramos el receptor
        registerReceiver(btReceiver, filter);
    }

    private void checkAndRequestPermissions() {
    //Pide permiso para que la app pueda encender el bluetooth
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN);
            }

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
        } else {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS
            );
        }
    }

    @Override
    protected void onDestroy() {
        //Borra los datos de la aplicación cuando se cierra
        super.onDestroy();

        if (bluetoothAdapter != null) {

            boolean discovering = false;

            try {
                discovering = bluetoothAdapter.isDiscovering();
            } catch (SecurityException e) {
                discovering = false;
            }

            if (discovering) {
                try {
                    bluetoothAdapter.cancelDiscovery();
                } catch (SecurityException e) {
                    // No pasa nada, simplemente no tenemos permiso
                }
            }
        }

        unregisterReceiver(btReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "Permisos de Bluetooth/ubicación necesarios para escanear",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
        }
    }
}