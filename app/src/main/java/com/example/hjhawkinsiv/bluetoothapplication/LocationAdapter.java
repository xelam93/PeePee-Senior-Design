package com.example.hjhawkinsiv.bluetoothapplication;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hjhawkinsiv on 10/17/2016.
 */

public class LocationAdapter extends ArrayAdapter<LocationItem>
{
    private Context context;
    private ArrayList<LocationItem> locations;

    public LocationAdapter(Context context, ArrayList<LocationItem> locItems)
    {
        super(context, R.layout.location_row, locItems);
        context = context;
        locations = locItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LocationItem loc = locations.get(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = layoutInflater.inflate(R.layout.location_row, parent, false);

        TextView long_lat = (TextView) rowView.findViewById(R.id.long_lat);

        long_lat.setText("(" + loc.getLongitude() + ", " + loc.getLatitude() + ")");

        return rowView;
    }
}
