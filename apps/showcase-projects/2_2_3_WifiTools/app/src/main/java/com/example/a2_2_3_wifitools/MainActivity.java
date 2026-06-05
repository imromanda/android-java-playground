package com.example.a2_2_3_wifitools;

import com.example.a2_2_3_wifitools.scanner.WifiScannerActivity;
import com.example.a2_2_3_wifitools.connector.WifiConnectorActivity;
import com.example.a2_2_3_wifitools.p2p.WifiP2PDiscoveryActivity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnScanner = findViewById(R.id.btnScanner);
        Button btnConnector = findViewById(R.id.btnConnector);
        Button btnP2P = findViewById(R.id.btnP2P);

        btnScanner.setOnClickListener(v ->
                startActivity(new Intent(this, WifiScannerActivity.class)));

        btnConnector.setOnClickListener(v ->
                startActivity(new Intent(this, WifiConnectorActivity.class)));

        btnP2P.setOnClickListener(v ->
                startActivity(new Intent(this, WifiP2PDiscoveryActivity.class)));
    }
}
