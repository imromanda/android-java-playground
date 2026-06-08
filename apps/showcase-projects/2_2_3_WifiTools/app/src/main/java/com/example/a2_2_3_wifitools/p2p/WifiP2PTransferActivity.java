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
 *  - Si somos Cliente → conectarse al GO y enviar texto
 *  - Mostrar mensajes enviados/recibidos
 */
public class WifiP2PTransferActivity extends BaseActivity {

    private boolean isGroupOwner;
    private String ownerIp;

    private TextView txtMessages;
    private EditText edtMessage;
    private Button btnSend;

    private Socket clientSocket; // Para el cliente
    private ServerSocket serverSocket; // Para el GO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_p2p_transfer);

        setupToolbar("P2P Transfer");

        txtMessages = findViewById(R.id.txtMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        // Recibimos los datos desde la Activity anterior
        isGroupOwner = getIntent().getBooleanExtra("isGroupOwner", false);
        ownerIp = getIntent().getStringExtra("ownerIp");

        if (isGroupOwner) {
            txtMessages.setText("Eres Group Owner.\nEsperando mensajes...");
            startServer();
        } else {
            txtMessages.setText("Eres Cliente.\nConectando al GO...");
            startClient();
        }

        btnSend.setOnClickListener(v -> {
            String msg = edtMessage.getText().toString();
            if (!msg.isEmpty()) {
                sendMessage(msg);
            }
        });
    }

    /**
     * Si somos GO → abrimos un ServerSocket y esperamos conexiones.
     */
    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8888);
                appendMessage("ServerSocket abierto en el puerto 8888");

                Socket socket = serverSocket.accept();
                appendMessage("Cliente conectado");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                String line;
                while ((line = reader.readLine()) != null) {
                    appendMessage("Cliente: " + line);
                }

            } catch (Exception e) {
                appendMessage("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Si somos Cliente → nos conectamos al GO.
     */
    private void startClient() {
        new Thread(() -> {
            try {
                appendMessage("Conectando a " + ownerIp + ":8888...");
                clientSocket = new Socket(ownerIp, 8888);
                appendMessage("Conectado al GO");

            } catch (Exception e) {
                appendMessage("Error al conectar: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Enviar mensaje desde el cliente al GO.
     */
    private void sendMessage(String msg) {
        new Thread(() -> {
            try {
                if (clientSocket == null) {
                    appendMessage("No hay conexión con el GO");
                    return;
                }

                OutputStream out = clientSocket.getOutputStream();
                out.write((msg + "\n").getBytes());
                out.flush();

                appendMessage("Tú: " + msg);

            } catch (Exception e) {
                appendMessage("Error al enviar: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Añadir texto al TextView desde cualquier hilo.
     */
    private void appendMessage(String msg) {
        runOnUiThread(() -> txtMessages.append(msg + "\n"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null) serverSocket.close();
            if (clientSocket != null) clientSocket.close();
        } catch (Exception ignored) {}
    }
}
