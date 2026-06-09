package com.example.a2_2_3_wifitools.p2p;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.TextView;

public class WifiP2PBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    // Gestor principal de WiFi Direct

    private WifiP2pManager.Channel channel;
    // Canal de comunicación con el framework WiFi Direct

    private WifiP2PDiscoveryActivity activity;
    // Referencia a la Activity principal de descubrimiento

    //CONSTRUCTOR DE LA CLASE WIFIP2PBROADCASTRECIVER
    public WifiP2PBroadcastReceiver(WifiP2pManager manager,
                                    WifiP2pManager.Channel channel,
                                    WifiP2PDiscoveryActivity activity) {
        this.manager = manager;
        // Guarda la referencia al manager

        this.channel = channel;
        // Guarda la referencia al canal

        this.activity = activity;
        // Guarda la referencia al activity

    }

    @Override
    public void onReceive(Context context, Intent intent) {
    // Méetodo llamado automáticamente cuando ocurre un evento registrado

        String action = intent.getAction();
        // Obtiene el tipo de evento recibido

        if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Comprueba si el evento corresponde a un cambio en la lista de dispositivos
            if (ActivityCompat.checkSelfPermission(
            //Verifica que hay permisos de ubicación
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Si no hay permiso, abandona el méetodo
                return;
            }

            manager.requestPeers(channel, (WifiP2pDeviceList peers) -> {
            // Solicita la lista actual de dispositivos encontrados
                activity.updateDeviceList(peers.getDeviceList());
                // Envía la lista de dispositivos a la Activity
            });
        }

        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
        // Comprueba si el evento corresponde a un cambio de conexión

            manager.requestConnectionInfo(channel, info -> {
            //Solicita información de la conexión establecida

                if (info.groupFormed) {
                // Comprueba si el grupo WiFi Direct ya está formado
                    if (info.isGroupOwner) {
                    //Comprueba si somos el Group Owner o el cliente para ejecutar el méetodo adecuado
                        activity.onGroupOwnerReady();
                        // Notifica a la Activity que actúe como servidor
                    } else {
                        activity.onClientReady(info.groupOwnerAddress);
                        //Notifica a la Activity para que actúe como ciente
                    }
                }
            });
        }



    }
}
