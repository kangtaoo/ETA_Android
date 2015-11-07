package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kangkang on 10/25/15.
 */
public class Location implements Parcelable{

    private String name;
    private String address;
    private double latitude;
    private double longitude;


    /**
     * Parcelable creator. Do not modify this function.
     */
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel p) {
            return new Location(p);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    /**
     * Create a Location model object from a Parcel. This
     * function is called via the Parcelable creator.
     *
     * @param p The Parcel used to populate the
     * Model fields.
     */
    public Location(Parcel p) {

        // TODO - fill in here
        this.name = p.readString();
        this.address = p.readString();
        this.longitude = p.readDouble();
        this.latitude = p.readDouble();
    }

    /**
     * Constructor with all fields set
     * */
    public Location(String name, String address,
                    double latitude, double longitude){
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Default constructor, do nothing
     * */
    public Location(){}

    /**
     * Serialize Person object by using writeToParcel.
     * This function is automatically called by the
     * system when the object is serialized.
     *
     * @param dest Parcel object that gets written on
     * serialization. Use functions to write out the
     * object stored via your member variables.
     *
     * @param flags Additional flags about how the object
     * should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     * In our case, you should be just passing 0.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // TODO - fill in here
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    /**
     * Do not implement
     */
    @Override
    public int describeContents() {
        // Do not implement!
        return 0;
    }

    public void setName(String name) {this.name = name;}
    public void setAddress(String address) {this.address = address;}
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public void setLongitude(double longitude) {this.longitude = longitude;}

    public String getName() { return this.name; }
    public String getAddress() { return this.address; }
    public double getLatitude() { return this.latitude; }
    public double getLongitude() { return this.longitude; }

}
