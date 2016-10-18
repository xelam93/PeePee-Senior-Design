package com.example.hjhawkinsiv.bluetoothapplication;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjhawkinsiv on 10/16/2016.
 */

public class ScannedDevices extends ListActivity
{
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothItem> scannedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        displayListOfScannedDevices();
    }

    private void displayListOfScannedDevices()
    {
        scannedDevices = new ArrayList<BluetoothItem>();

        bluetoothAdapter.startDiscovery();

        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                    BluetoothItem item = new BluetoothItem();

                    item.set("name", device.getName());
                    item.set("address", device.getAddress());
                    item.set("state", device.getBondState());
                    item.set("type", device.getType());
                    item.set(device.getUuids());
                    item.set("rssi", rssi);

                    scannedDevices.add(item);

                    ScannedAdapter scannedAdapter = new ScannedAdapter(getApplicationContext(), scannedDevices);

                    setListAdapter(scannedAdapter);

                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        bluetoothAdapter.cancelDiscovery();
    }
}
