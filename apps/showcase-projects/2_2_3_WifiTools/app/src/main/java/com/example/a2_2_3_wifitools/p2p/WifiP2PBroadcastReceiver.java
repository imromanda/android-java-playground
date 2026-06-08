package com.example.a2_2_3_wifitools.p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.TextView;

public class WifiP2PBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiP2PDiscoveryActivity activity;

    public WifiP2PBroadcastReceiver(WifiP2pManager manager,
                                    WifiP2pManager.Channel channel,
                                    WifiP2PDiscoveryActivity activity) {
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // Solicita la lista de dispositivos P2P disponibles
            manager.requestPeers(channel, (WifiP2pDeviceList peers) -> {
                activity.updateDeviceList(peers.getDeviceList());
            });
        }
    }
}
