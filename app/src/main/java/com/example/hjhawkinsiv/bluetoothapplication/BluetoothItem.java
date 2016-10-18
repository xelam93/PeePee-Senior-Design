package com.example.hjhawkinsiv.bluetoothapplication;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

/**
 * Created by hjhawkinsiv on 10/16/2016.
 */

public class BluetoothItem implements Parcelable
{
    private String name;
    private String addr;
    private ParcelUuid[] uuids;
    private int state;
    private int type;
    private int rssi;

    public BluetoothItem() {}

    public BluetoothItem(Parcel inParcel)
    {
        super();
        readFromParcel(inParcel);
    }

    public String get(String var)
    {
        switch (var = var.toLowerCase())
        {
            case "name":
                return name;
            case "address":
                return addr;
            case "state":
                return String.valueOf(state);
            case "type":
                return String.valueOf(type);
            case "uuid":
                if(uuids != null) {
                    return String.valueOf(uuids[0]);
                }
                else
                {
                    return null;
                }
            case "rssi":
                return String.valueOf(rssi);
        }

        return null;
    }

    public void set(String var, String set_name)
    {
        switch(var = var.toLowerCase())
        {
            case "name":
                name = set_name;
                break;
            case "address":
                addr = set_name;
                break;
        }
    }

    public void set(String var, int set_int)
    {
        switch(var = var.toLowerCase())
        {
            case "state":
                state = set_int;
                break;
            case "type":
                type = set_int;
                break;
            case "rssi":
                rssi = set_int;
                break;
        }
    }

    public void set(ParcelUuid set_uuids[])
    {
        uuids = set_uuids;
    }

    public void readFromParcel(Parcel inParcel)
    {
        name = inParcel.readString();
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags)
    {
        outParcel.writeString(name);
    }

    @Override
    public int describeContents()
    {
        return -1;
    }

    public static final Parcelable.Creator<BluetoothItem> CREATOR = new Parcelable.Creator<BluetoothItem>()
    {
        public BluetoothItem createFromParcel(Parcel inParcel)
        {
            return new BluetoothItem(inParcel);
        }

        public BluetoothItem[] newArray(int length)
        {
            return new BluetoothItem[length];
        }
    };
}
