package com.example.serviciosweb_1_9;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private ProgressBar progressBar;

    // Quitamos TextView y lo sustituimos por la lista que guarda los usuarios
    private ListView listViewUsers;
    private List<User> userList; // guardamos la lista para usarla en el clic

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        listViewUsers = findViewById(R.id.listViewUsers);

        progressBar.setVisibility(View.VISIBLE);
        listViewUsers.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<User>> call = apiService.getUsers();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                progressBar.setVisibility(View.GONE);
                listViewUsers.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    userList = response.body();
                    List<String> names = new ArrayList<>();
                    if (userList != null) {
                        for (User user : userList) {
                            names.add(user.getName());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            names
                    );
                    listViewUsers.setAdapter(adapter);

                    listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
                        User selectedUser = userList.get(position);
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("id", selectedUser.getId());
                        intent.putExtra("name", selectedUser.getName());
                        intent.putExtra("email", selectedUser.getEmail());
                        startActivity(intent);
                    });

                } else {
                    Toast.makeText(MainActivity.this,
                            "Error HTTP: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,
                        "Error de red: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Network Error", t);

            }
        });
    }
}
