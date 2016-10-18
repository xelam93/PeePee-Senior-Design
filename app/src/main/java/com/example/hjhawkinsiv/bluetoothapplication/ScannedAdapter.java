package com.example.hjhawkinsiv.bluetoothapplication;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hjhawkinsiv on 10/16/2016.
 */

public class ScannedAdapter extends ArrayAdapter<BluetoothItem>
{
    private Context context;
    private ArrayList<BluetoothItem> scannedDevices;

    public ScannedAdapter(Context context, ArrayList<BluetoothItem> scannedDevices)
    {
        super(context, R.layout.scan_bt_row, scannedDevices);

        this.context = context;
        this.scannedDevices = scannedDevices;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BluetoothItem bluetoothItem = scannedDevices.get(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = layoutInflater.inflate(R.layout.scan_bt_row, parent, false);

        TextView scan_name = (TextView) rowView.findViewById(R.id.scan_name);
        TextView scan_addr = (TextView) rowView.findViewById(R.id.scan_addr);
        TextView scan_state = (TextView) rowView.findViewById(R.id.scan_state);
        TextView scan_type = (TextView) rowView.findViewById(R.id.scan_type);
        TextView scan_uuid = (TextView) rowView.findViewById(R.id.scan_uuid);
        TextView scan_rssi = (TextView) rowView.findViewById(R.id.scan_rssi);

        scan_name.setText(bluetoothItem.get("name"));
        scan_addr.setText("Address: " + bluetoothItem.get("address"));
        scan_state.setText("State: " + bluetoothItem.get("state"));
        scan_type.setText("Type: " + bluetoothItem.get("type"));
        scan_rssi.setText("RSSI: " + bluetoothItem.get("rssi"));

        String uuid = bluetoothItem.get("uuid");

        if(uuid != null)
            scan_uuid.setText("UUID: " + uuid);

        return rowView;
    }
}
