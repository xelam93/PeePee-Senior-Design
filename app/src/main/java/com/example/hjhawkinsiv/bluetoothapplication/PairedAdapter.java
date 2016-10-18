package com.example.hjhawkinsiv.bluetoothapplication;

import android.content.Context;
import android.text.Layout;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hjhawkinsiv on 10/16/2016.
 */

public class PairedAdapter extends ArrayAdapter<BluetoothItem>
{
    private Context context;
    private ArrayList<BluetoothItem> pairedDevices;

    public PairedAdapter(Context context, ArrayList<BluetoothItem> pairedDevices)
    {
        super(context, R.layout.paired_bt_row, pairedDevices);
        this.context = context;
        this.pairedDevices = pairedDevices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BluetoothItem bluetoothItem = pairedDevices.get(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = layoutInflater.inflate(R.layout.paired_bt_row, parent, false);

        TextView pair_name = (TextView) rowView.findViewById(R.id.pair_name);
        TextView pair_addr = (TextView) rowView.findViewById(R.id.pair_addr);
        TextView pair_state = (TextView) rowView.findViewById(R.id.pair_state);
        TextView pair_type = (TextView) rowView.findViewById(R.id.pair_type);

        pair_name.setText(bluetoothItem.get("name"));
        pair_addr.setText("Address: " + bluetoothItem.get("address"));
        pair_state.setText("State: " + bluetoothItem.get("state"));
        pair_type.setText("Type: " + bluetoothItem.get("type"));

        return rowView;
    }
}
