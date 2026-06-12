package com.example.my_meteo_app;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherIconsActivity extends AppCompatActivity {
    ImageView imgIcon;
    TextView txtTemperature;
    TextView txtWindSpeed;
    TextView txtEstado;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons_activity);


        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener( view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        Button btnConsultar_Clima_URL = findViewById(R.id.btnConsultar_Clima_URL);
        btnConsultar_Clima_URL.setOnClickListener(v->{
            fetchWeather();
        });
         imgIcon = findViewById(R.id.imgIcon);
         txtTemperature = findViewById(R.id.txtTemperature);
         txtWindSpeed = findViewById(R.id.txtWindSpeed);
         txtEstado = findViewById(R.id.txtEstado);
         txtResult = findViewById(R.id.txtResult);

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
                    try {
                        URL url = new URL(weatherUrl);

                        HttpURLConnection conection = (HttpURLConnection) url.openConnection();

                        conection.setRequestMethod("GET");
                        conection.setReadTimeout(5000);
                        conection.setConnectTimeout(5000);

                        int response;
                        response = conection.getResponseCode();

                        if (response == HttpURLConnection.HTTP_OK) {
                            InputStream input = conection.getInputStream();


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
                            int isDay = todaysWeather.getInt("is_day");
                            int weatherCode = todaysWeather.getInt("weathercode");

                            buffered.close();
                            int iconRes = WeatherIconMapper.getIcon(weatherCode, isDay);


                            runOnUiThread(
                                    () -> {
                                        imgIcon.setImageResource(iconRes);
                                        txtTemperature.setText("Temperature: " + String.valueOf(temp) + "ºC");
                                        txtWindSpeed.setText("Velocidad del viento: " + String.valueOf(vel) + "Km/h");
                                        txtEstado.setText("Código de tiempo: " + String.valueOf(weatherCode));
                                    });
                        } else {
                            runOnUiThread(
                                    () -> {
                                        txtResult.setVisibility(VISIBLE);
                                        txtResult.setText("Error en la conexión");
                                    });
                        }

                    } catch (MalformedURLException e) {
                        runOnUiThread(
                                () -> {
                                    txtResult.setVisibility(VISIBLE);
                                    txtResult.setText("Error en la URL");
                                });

                    } catch (IOException e) {
                        runOnUiThread(
                                () -> {
                                    txtResult.setVisibility(VISIBLE);
                                    txtResult.setText("Error de conexión o lectura de datos");
                                });

                    } catch (org.json.JSONException e) {
                        runOnUiThread(
                                () -> {
                                    txtResult.setVisibility(VISIBLE);
                                    txtResult.setText("Error al interpretar el JSON");
                                });
                    }
                })
                .start();
    }

}
