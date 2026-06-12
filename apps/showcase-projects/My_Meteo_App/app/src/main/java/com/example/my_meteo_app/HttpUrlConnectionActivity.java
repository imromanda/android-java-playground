package com.example.my_meteo_app;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpUrlConnectionActivity extends AppCompatActivity {

    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_url_http);

        txtResult = findViewById(R.id.txtResult);
        Button btnConsultar_Clima_URL = findViewById(R.id.btnConsultar_Clima_URL);
        //Enlazamos el botón

        btnConsultar_Clima_URL.setOnClickListener(v->{
            //Listener para el botón
            //Aquí irá la petición
     fetchWeather();
        });

        Button btnBack = findViewById(R.id.btnBack);
        //Obtiene referencia al primer botón
        btnBack.setOnClickListener( view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    private void fetchWeather() {
        // Creamos la URL completa a partir de una cadena de texto
        String baseUrl = "https://api.open-meteo.com/v1/forecast";
        String latitude = "?latitude=36.84";
        String longitude = "&longitude=-2.46";
        String currentWeather = "&current_weather=true";
        String weatherUrl = baseUrl + latitude + longitude + currentWeather;
    // Esta URL incluye la base del endpoint y los parámetros necesarios (latitud, longitud y tipo
    // de datos)

    new Thread(
            () -> {
              // aquí va la petición HTTP
              try {
                URL url = new URL(weatherUrl);
                // La URL por sí sola es solo un String, no permite conexión con Internet
                // Por eso la convertimos en un objeto URL de Java

                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                // Abrimos una conexión HTTP a la dirección indicada en la URL
                // openConnection() devuelve un objeto genérico (URLConnection)

                // Convertimos la conexión genérica (URLConnection) a HttpURLConnection a la fuerza,
                // con los paréntesis antes de url.openConnection();
                // Esto es necesario porque necesitamos funcionalidades específicas de HTTP (como
                // GET, códigos de respuesta, etc.)

                // Ahora tenemos una conexión lista para configurar (méeetodo GET, timeouts, etc.)
                // y para realizar la petición al servidor Open-Meteo

                conection.setRequestMethod("GET");
                // Méetodo get para hacer petición

                conection.setReadTimeout(5000);
                // Timeout para leer la petición de 5 segundos
                conection.setConnectTimeout(5000);
                // Timeout para la conexión de 5 segundos

                int response;
                response = conection.getResponseCode();
                // Obtenemos el código de respuesta para el if

                if (response == HttpURLConnection.HTTP_OK) {
                  InputStream input = conection.getInputStream();
                  // Trae el stream de datos en bytes y lo guarda en una variable input de tipo
                  // InputStream

                  InputStreamReader reader = new InputStreamReader(input);
                  BufferedReader buffered = new BufferedReader(reader);
                  StringBuilder result = new StringBuilder();

                  String line;

                  while ((line = buffered.readLine()) != null) {
                    result.append(line).append("\n");
                  }
                  JSONObject wholeJson = new JSONObject(result.toString());
                  String wholeJsonString = wholeJson.toString(6);
                  JSONObject todaysWeather = (wholeJson.getJSONObject("current_weather"));

                  double temp = todaysWeather.getDouble("temperature");
                  double vel = todaysWeather.getDouble("windspeed");
                  int isday = todaysWeather.getInt("is_day");
                  int weatherCode = todaysWeather.getInt("weathercode");
                  String timeOfDay = (isday == 1 ? "Día" : "Noche");
                  String weatherOfToday =
                      "\nTemperatura: " + temp + "ºC" +
                      "\nVelocidad del viento: " + vel + "Km/h" +
                      "\nDía o noche? " + timeOfDay +
                      "\nCódigo de tiempo: " + weatherCode;

                  buffered.close();
                  runOnUiThread(
                      () -> {
                        txtResult.setText(weatherOfToday);
                        //  txtResult.setText(wholeJsonString);
                      });
                } else {
                  txtResult.setText("Error en la conexión");
                }

              } catch (MalformedURLException e) {
                runOnUiThread(
                    () -> {
                      txtResult.setText("Error en la URL");
                    });

              } catch (IOException e) {
                runOnUiThread(
                    () -> {
                      txtResult.setText("Error de conexión o lectura de datos");
                    });

              } catch (org.json.JSONException e) {
                runOnUiThread(
                    () -> {
                      txtResult.setText("Error al interpretar el JSON");
                    });
              }
            })
        .start();
    }



        }
