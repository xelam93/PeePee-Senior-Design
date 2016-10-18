package com.example.hjhawkinsiv.bluetoothapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjhawkinsiv on 10/16/2016.
 */

public class PairedDevices extends ListActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        ArrayList<BluetoothItem> pairedDevices = intent.getParcelableArrayListExtra("listOfPairedDevices");

        PairedAdapter pairedAdapter = new PairedAdapter(getApplicationContext(), pairedDevices);

        setListAdapter(pairedAdapter);
    }
}
