package com.hfad.myfilmsearcher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ConnectionStateMonitor extends LiveData<Boolean> {

    private Context context;
    private ConnectivityManager.NetworkCallback networkCallback = null;
    private NetworkReciever networkReciever;
    private ConnectivityManager connectivityManager;

    public ConnectionStateMonitor(Context mContext) {
        context = mContext;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = new NetworkCallback(this);
        } else {
            networkReciever = new NetworkReciever();
        }
    }

    @Override
    protected void onActive() {
        super.onActive();
        updateConnection();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        } else {
            context.registerReceiver(networkReciever, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        } else {
            context.unregisterReceiver(networkReciever);
        }
    }

    class NetworkCallback extends ConnectivityManager.NetworkCallback {
        private ConnectionStateMonitor connectionStateMonitor;

        public NetworkCallback(ConnectionStateMonitor mConnectionStateMonitor) {
            connectionStateMonitor = mConnectionStateMonitor;
        }

        @Override
        public void onAvailable(@NonNull Network network) {
            if (connectivityManager.isActiveNetworkMetered()) {
                Toast.makeText(context, "MobileInternet Onn", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Wifi Onn", Toast.LENGTH_SHORT).show();
            }
            if (network != null) {
                connectionStateMonitor.postValue(true);
            }
        }

        @Override
        public void onLost(@NonNull Network network) {
            Toast.makeText(context, "Internet is unavailable", Toast.LENGTH_SHORT).show();
            connectionStateMonitor.postValue(false);
        }
    }

    private void updateConnection() {
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                postValue(true);
            } else {
                postValue(false);
            }
        }
    }

    class NetworkReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                updateConnection();
            }
        }
    }
}
