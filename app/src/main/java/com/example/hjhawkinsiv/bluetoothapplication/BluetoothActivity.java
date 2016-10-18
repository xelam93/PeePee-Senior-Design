package com.example.hjhawkinsiv.bluetoothapplication;

import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener
{
    private BluetoothAdapter bluetoothAdapter;
    private GoogleApiClient googleApiClient;

    private Location lastLocation;
    private LocationRequest locationRequest;

    private int REQUEST_BLUETOOTH = 1;
    private int SUCCESFUL_CONNECTION = -1;

    private final int PERMISSION_REQUEST = 1;
    private final int UPDATE_INTERVAL = 1*30*1000;      //30 sec. update interval
    private final int UPDATE_FAST_INTERVAL = 1*10*1000; //10 sec. fastest update interval

    private int permission = -1000;
    private int connection_trials = 0;

    private Button enable_button;
    private Button list_button;
    private Button scan_button;
    private Button loc_button;
    private Button loclist_button;

    private ArrayList<LocationItem> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        enable_button = (Button) findViewById(R.id.enable_button);
        list_button = (Button) findViewById(R.id.list_button);
        scan_button = (Button) findViewById(R.id.scan_button);
        loc_button = (Button) findViewById(R.id.loc_button);
        loclist_button = (Button) findViewById(R.id.loclist_button);

        enable_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enableBluetooth();
            }
        });

        list_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(bluetoothAdapter.isEnabled())
                {
                    if(listOfPairedDevices() != null)
                    {
                        Intent intent = new Intent(BluetoothActivity.this, PairedDevices.class);
                        intent.putParcelableArrayListExtra("listOfPairedDevices", listOfPairedDevices());
                        startActivity(intent);
                    }
                }
            }
        });

        scan_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(bluetoothAdapter.isEnabled()) {
                    scanForBluetoothDevices();
                }

            }
        });

        loc_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                updateLocation();
            }
        });

        loclist_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(locations.size() > 0)
                {
                    Intent intent = new Intent(BluetoothActivity.this, Locations.class);
                    intent.putParcelableArrayListExtra("Locations", locations);
                    startActivity(intent);
                }
            }
        });

        if(locations == null)
            locations = new ArrayList<LocationItem>();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(UPDATE_FAST_INTERVAL);

        if(googleApiClient == null)
        {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data)
    {
        super.onActivityResult(request, result, data);

        if(request == REQUEST_BLUETOOTH)
        {
            if(result == 0)
            {
                Toast.makeText(this, "Bluetooth not enabled.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Bluetooth enabled.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        SUCCESFUL_CONNECTION = 1;
        connection_trials = 0;

        Toast.makeText(this, "Successfully connected to Google Play Services.", Toast.LENGTH_LONG).show();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (lastLocation == null)
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            else
            {
                Toast.makeText(this, "Last location received.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Toast.makeText(this, "Google Play Services failed to connect.", Toast.LENGTH_LONG).show();
        SUCCESFUL_CONNECTION = -1;
    }

    public void onConnectionSuspended(int cause)
    {
        googleApiClient.connect();

        connection_trials++;

        if(connection_trials >= 25) {
            Toast.makeText(this, "Attempted to connect 25 times. Stopping trials.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(googleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
            permission = -1000;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        updateLocation(lastLocation);

    }

    @Override
    public void onRequestPermissionsResult(int request, String permissions[], int[] grants)
    {
        switch(request)
        {
            case PERMISSION_REQUEST:
            {
                if(grants.length > 0 && grants[0] == PackageManager.PERMISSION_GRANTED)
                {
                    permission = 1000;
                }
                else
                {
                    permission = 0;
                }
                return;
            }
        }
    }
    private void enableBluetooth()
    {
        if(bluetoothAdapter == null)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Incompatible Device")
                    .setMessage("Bluetooth is not supported on this device.")
                    .setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_BLUETOOTH);
        }
    }

    private ArrayList<BluetoothItem> listOfPairedDevices()
    {
        ArrayList<BluetoothItem> pairedDevicesList = null;

        Set<BluetoothDevice> pairedDevicesSet = bluetoothAdapter.getBondedDevices();

        if(pairedDevicesSet.size() > 0)
        {
            pairedDevicesList = new ArrayList<BluetoothItem>();

            for(BluetoothDevice device : pairedDevicesSet)
            {
                BluetoothItem item = new BluetoothItem();

                item.set("name", device.getName());
                item.set("address", device.getAddress());
                item.set("state", device.getBondState());
                item.set("type", device.getType());
                item.set(device.getUuids());

                pairedDevicesList.add(item);
            }
        }

        return pairedDevicesList;
    }

    private void scanForBluetoothDevices()
    {
        Intent intent = new Intent(this, ScannedDevices.class);
        startActivity(intent);
    }

    private void updateLocation()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST);
        }
        else
        {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            updateLocation(location);
        }
    }

    private void updateLocation(Location location)
    {
        if(location != null)
        {
            locations.add(new LocationItem(location.getLongitude(), location.getLatitude()));

            Toast.makeText(this, "(" + location.getLongitude() + ", " + location.getLatitude() + ") added to locations.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Failed to update location.", Toast.LENGTH_LONG).show();
        }
    }
}

