package com.example.hjhawkinsiv.bluetoothapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by hjhawkinsiv on 10/17/2016.
 */

public class Locations extends ListActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        ArrayList<LocationItem> locationItems = intent.getParcelableArrayListExtra("Locations");

        LocationAdapter locationAdapter = new LocationAdapter(getApplicationContext(), locationItems);

        setListAdapter(locationAdapter);
    }
}
