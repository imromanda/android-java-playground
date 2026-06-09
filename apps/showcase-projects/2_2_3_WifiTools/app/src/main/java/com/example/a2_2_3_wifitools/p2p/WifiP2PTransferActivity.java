package com.example.a2_2_3_wifitools.p2p;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a2_2_3_wifitools.R;
import com.example.a2_2_3_wifitools.core.BaseActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Activity encargada de:
 *  - Si somos Group Owner → abrir un ServerSocket y recibir texto
 *  - Si somos Cliente → conectarse al Group Owner y enviar texto
 *  - Mostrar mensajes enviados/recibidos
 */

public class WifiP2PTransferActivity extends BaseActivity {

    private boolean isGroupOwner;
    // Indica si este dispositivo es el Group Owner (servidor)
    private String ownerIp;
    // Guarda la IP del owner


    private TextView txtMessages;
    // Área donde se muestran los mensajes

    private EditText edtMessage;
    // Input de texto

    private Button btnSend;
    //Botón de enviar

    private Socket clientSocket; // Para el cliente
    // Socket utilizado por el cliente para conectarse al Group Owner

    private ServerSocket serverSocket; // Para el Group Owner
    // Socket servidor utilizado por el Group Owner


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Llama al méetodo onCreate del padre

        setContentView(R.layout.activity_wifi_p2p_transfer);
        //Localiza el layout correspondiente a esta actividad

        setupToolbar("P2P Transfer");
        // Configura el título de la barra superior

        //Localiza los componentes del layout
        txtMessages = findViewById(R.id.txtMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        isGroupOwner = getIntent().getBooleanExtra("isGroupOwner", false);
        // Obtiene si somos Group Owner desde la Activity anterior

        ownerIp = getIntent().getStringExtra("ownerIp");
        // Obtiene la IP del owner

        if (isGroupOwner) {
        //Si somos el Group Owner
            txtMessages.setText("Eres Group Owner.\nEsperando mensajes...");
            startServer();
            //Muestra el mensaje e inicia el servidor -> Group Owner
        } else {
        //Si no,
            txtMessages.setText("Eres Cliente.\nConectando al Group Owner...");
            //Muestra el mensaje e inicia la conexión con el servidor -> Client
            startClient();
        }

        btnSend.setOnClickListener(v -> {
        //Lógica para el botón Send
            String msg = edtMessage.getText().toString();
            // Obtiene el texto escrito
            if (!msg.isEmpty()) {
                // Comprueba que no esté vacío
                sendMessage(msg);
                //Envía el texto escrito
            }
        });
    }

    /**
     * Si somos Group Owner → abrimos un ServerSocket y esperamos conexiones.
     */
    private void startServer() {
    //El Group Owner abre el ServerSocket y espera las conexiones
        new Thread(() -> {
        // Ejecuta el servidor en un hilo secundario ****************************** MUY IMPORTANTE!!!
            try {
                serverSocket = new ServerSocket(8888);
                // Abre un servidor escuchando en el puerto 8888
                appendMessage("ServerSocket abierto en el puerto 8888");
                //Muestra el mensaje

                Socket socket = serverSocket.accept();
                // Espera hasta que un cliente se conecte
                appendMessage("Cliente conectado");
                //Muestra el mensaje

                BufferedReader reader = new BufferedReader(
                        // Crea un lector para recibir mensajes
                        new InputStreamReader(socket.getInputStream())
                );

                String line;
                while ((line = reader.readLine()) != null) {
                // Lee mensajes continuamente mientras existan
                    appendMessage("Cliente: " + line);
                    // Muestra cada mensaje recibido
                }

            } catch (Exception e) {
                // Muestra errores del servidor
                appendMessage("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Si somos Cliente → nos conectamos al Group Owner.
     */
    private void startClient() {
        //Si somos el cliente nos conectamos al Group Owner
        new Thread(() -> {
        // Ejecuta el servidor en un hilo secundario ****************************** MUY IMPORTANTE!!!

            try {
                appendMessage("Conectando a " + ownerIp + ":8888...");
                // Informa de la conexión
                clientSocket = new Socket(ownerIp, 8888);
                // Conecta con el servidor Group Owner
                appendMessage("Conectado al Group Owner");
                //Informa de la conexión

            } catch (Exception e) {
                // Muestra errores del servidor
                appendMessage("Error al conectar: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Enviar mensaje desde el cliente al Group Owner.
     */
    private void sendMessage(String msg) {
        new Thread(() -> {
        // Ejecuta el envío en segundo plano
            try {
                if (clientSocket == null) {
                    // Comprueba que exista conexión
                    appendMessage("No hay conexión con el Group Owner");
                    //Informa y sale de la función
                    return;
                }
                OutputStream out = clientSocket.getOutputStream();
                // Obtiene el flujo de salida del socket
                out.write((msg + "\n").getBytes());
                // Convierte el texto en bytes y lo envía
                out.flush();
                // Fuerza el envío inmediato

                appendMessage("Tú: " + msg);
                // Muestra el mensaje enviado

            } catch (Exception e) {
                //Muestra error al enviar el mensaje
                appendMessage("Error al enviar: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Añadir texto al TextView desde cualquier hilo.
     */
    private void appendMessage(String msg) {
        runOnUiThread(() -> txtMessages.append(msg + "\n"));
        // Ejecuta la actualización en el hilo principal de la UI
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null) serverSocket.close();
            //Si la conexión con el servidor es distinto de nulo, cierra la conexión
            // Cierra el servidor si existe

            if (clientSocket != null) clientSocket.close();
            // Cierra la conexión cliente si existe
            //Si la conexión con el cliente es distinto de nulo, cierra la conexión
        } catch (Exception ignored) {}
        // Ignora errores al cerrar conexiones
    }
}
