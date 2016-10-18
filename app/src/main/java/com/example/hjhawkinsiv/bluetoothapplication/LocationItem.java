package com.example.hjhawkinsiv.bluetoothapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjhawkinsiv on 10/17/2016.
 */

public class LocationItem implements Parcelable
{
    private double longitude;
    private double latitude;

    private String inParcel;

    public LocationItem(double longi, double lati)
    {
        longitude = longi;
        latitude = lati;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    //Parcelable Implementation

    public LocationItem(Parcel in)
    {
        double[] ll = new double[2];

        in.readDoubleArray(ll);

        longitude = ll[0];
        latitude = ll[1];
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeDoubleArray(new double[] {this.longitude, this.latitude});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public LocationItem createFromParcel(Parcel in)
        {
            return new LocationItem(in);
        }

        public LocationItem[] newArray(int length)
        {
            return new LocationItem[length];
        }
    };
}

