package com.example.serviciosweb_1_9;
// Importación de la anotación @NonNull para indicar que un parámetro no puede ser null
import androidx.annotation.NonNull;
// Clase base para Activities compatibles con Android moderno
import androidx.appcompat.app.AppCompatActivity;
// Necesario para abrir otra Activity
import android.content.Intent;
// Permite recibir el estado guardado de la Activity
import android.os.Bundle;
// Permite escribir mensajes en Logcat
import android.util.Log;
// Permite cambiar visibilidad de elementos y otras operaciones de interfaz
import android.view.View;
// Adaptador para mostrar listas simples de Strings
import android.widget.ArrayAdapter;
// Componente visual para mostrar listas
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
// Lista dinámica
import java.util.ArrayList;
// Interfaz List
import java.util.List;
// Clase Retrofit para peticiones HTTP
import retrofit2.Call;
// Callback que recibe la respuesta
import retrofit2.Callback;
// Objeto Response devuelto por Retrofit
import retrofit2.Response;
// Constructor principal de Retrofit
import retrofit2.Retrofit;
// Conversor JSON -> Objetos Java
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // ===== DECLARACIÓN DE VARIABLES Y COMPONENTES UTILIZADOS EN TODA LA ACTIVITY =====
    // URL base de la API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    // Referencia al ProgressBar
    private ProgressBar progressBar;
    // Referencia al ListView donde se mostrarán los usuarios
    private ListView listViewUsers;
    // Lista que almacenará los usuarios descargados
    private List<User> userList;


    // Meetodo que se ejecuta al abrir la Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Llama al Meetodo de la clase padre
        super.onCreate(savedInstanceState);
        // Carga el layout activity_main.xml
        setContentView(R.layout.activity_main);
        // Obtiene la referencia al ProgressBar definido en XML
        progressBar = findViewById(R.id.progressBar);
        // Obtiene la referencia al ListView definido en XML
        listViewUsers = findViewById(R.id.listViewUsers);
        // Muestra el indicador de carga
        progressBar.setVisibility(View.VISIBLE);
        // Oculta la lista mientras se descargan los datos
        listViewUsers.setVisibility(View.GONE);

        // Construcción del objeto Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                // URL base de la API
                .baseUrl(BASE_URL)
                // Conversor JSON -> Java
                .addConverterFactory(GsonConverterFactory.create())
                // Construye la instancia Retrofit
                .build();

        // Crea una implementación automática de ApiService
        ApiService apiService = retrofit.create(ApiService.class);
        // Prepara la petición GET /users
        Call<List<User>> call = apiService.getUsers();

        // Ejecuta la petición de forma asíncrona
        call.enqueue(new Callback<>() {
            // Se ejecuta cuando llega una respuesta del servidor
            @Override
            public void onResponse(@NonNull Call<List<User>> call,
                                   @NonNull Response<List<User>> response) {
                // Oculta el ProgressBar
                progressBar.setVisibility(View.GONE);
                // Muestra la lista
                listViewUsers.setVisibility(View.VISIBLE);
                // Comprueba si la respuesta HTTP es correcta (200 OK)
                if (response.isSuccessful()) {
                    // Obtiene la lista de usuarios recibida
                    userList = response.body();
                    // Lista que contendrá únicamente los nombres
                    List<String> names = new ArrayList<>();
                    // Verifica que la lista no sea null
                    if (userList != null) {
                        // Recorre todos los usuarios descargados
                        for (User user : userList) {
                            // Añade cada nombre a la lista names
                            names.add(user.getName());
                        }
                    }
                    // Adaptador que conecta la lista de nombres con el ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            // Contexto actual
                            MainActivity.this,
                            // Layout estándar de Android para una línea
                            android.R.layout.simple_list_item_1,
                            // Datos que se mostrarán
                            names
                    );

                    // Asocia el adaptador al ListView
                    listViewUsers.setAdapter(adapter);
                    // Evento que se ejecuta al pulsar un usuario
                    listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
                        // Obtiene el usuario pulsado
                        User selectedUser = userList.get(position);
                        // Crea un Intent para abrir DetailActivity
                        Intent intent = new Intent(MainActivity.this,
                                DetailActivity.class);
                        // Envía el ID del usuario
                        intent.putExtra("id", selectedUser.getId());

                        // Envía el nombre del usuario
                        intent.putExtra("name", selectedUser.getName());

                        // Envía el email del usuario
                        intent.putExtra("email", selectedUser.getEmail());

                        // Abre la segunda Activity
                        startActivity(intent);
                    });

                } else {

                    // Si la respuesta HTTP no es correcta muestra un mensaje
                    Toast.makeText(MainActivity.this,
                            "Error HTTP: " + response.code(),
                            Toast.LENGTH_SHORT).show();

                    // Registra el error en Logcat
                    Log.e("MainActivity",
                            "Error HTTP: " + response.code());
                }
            }

            // Se ejecuta cuando hay fallo de conexión
            @Override
            public void onFailure(@NonNull Call<List<User>> call,
                                  @NonNull Throwable t) {

                // Oculta el ProgressBar
                progressBar.setVisibility(View.GONE);

                // Muestra el error al usuario
                Toast.makeText(MainActivity.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();

                // Guarda información detallada del error en Logcat
                Log.e("MainActivity", "Network Error", t);
            }
        });
    }
}