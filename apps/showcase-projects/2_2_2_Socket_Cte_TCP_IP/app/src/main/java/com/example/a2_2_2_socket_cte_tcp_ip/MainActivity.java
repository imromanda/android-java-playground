package com.example.a2_2_2_socket_cte_tcp_ip;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText etIp, etPuerto, etMensaje;
    private Button btnEnviar;
    private TextView tvRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etIp = findViewById(R.id.etIp);
        etPuerto = findViewById(R.id.etPuerto);
        etMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        tvRespuesta = findViewById(R.id.tvRespuesta);

        btnEnviar.setOnClickListener(v -> enviarMensaje());
    }

    private void enviarMensaje() {

        String ip = etIp.getText().toString().trim();
        String puertoStr = etPuerto.getText().toString().trim();
        String mensaje = etMensaje.getText().toString().trim();

        if (ip.isEmpty() || puertoStr.isEmpty() || mensaje.isEmpty()) {
            tvRespuesta.setText("Rellena IP, puerto y mensaje.");
            return;
        }

        int puerto = Integer.parseInt(puertoStr);

        // HILO SECUNDARIO PARA LA CONEXIÓN TCP
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, puerto);

                // Enviar mensaje
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(mensaje);

                // Recibir respuesta
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                String respuesta = in.readLine();

                socket.close();

                // Actualizar UI (solo desde el hilo principal)
                runOnUiThread(() -> tvRespuesta.setText("Servidor dice: " + respuesta));

            } catch (Exception e) {
                runOnUiThread(() ->
                        tvRespuesta.setText("Error: " + e.getMessage())
                );
            }
        }).start();
    }
}
