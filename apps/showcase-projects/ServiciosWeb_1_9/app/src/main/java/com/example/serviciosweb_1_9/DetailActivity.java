package com.example.serviciosweb_1_9;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);

            TextView tvDetailName = findViewById(R.id.tvDetailName);
            TextView tvDetailEmail = findViewById(R.id.tvDetailEmail);
            TextView tvDetailId = findViewById(R.id.tvDetailId);

            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            int id = intent.getIntExtra("id", -1);

            tvDetailName.setText(name);
            tvDetailEmail.setText("Email: " + email);
            tvDetailId.setText("ID: " + id);
        }
    }

