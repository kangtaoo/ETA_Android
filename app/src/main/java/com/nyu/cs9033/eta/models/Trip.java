package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable {
	
	// Member fields should exist here, what else do you need for a trip?
	// Please add additional fields
	private String name;
	private String date;
	private String location;
	private Person friend;
	
	/**
	 * Parcelable creator. Do not modify this function.
	 */
	public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
		public Trip createFromParcel(Parcel p) {
			return new Trip(p);
		}

		public Trip[] newArray(int size) {
			return new Trip[size];
		}
	};
	
	/**
	 * Create a Trip model object from a Parcel. This
	 * function is called via the Parcelable creator.
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Trip(Parcel p) {
		
		// TODO - fill in here
		System.out.println("==========Trip::Trip(Parcel p)==========");
		this.name = p.readString();
		this.date = p.readString();
		this.location = p.readString();
		this.friend = p.readParcelable(Person.class.getClassLoader());
	}
	
	/**
	 * Create a Trip model object from arguments
	 * 
	 * @param name  Add arbitrary number of arguments to
	 * instantiate Trip class based on member variables.
	 */
	public Trip(String name, String date, String location, Person friend) {
		
		// TODO - fill in here, please note you must have more arguments here
		this.name = name;
		this.date = date;
		this.location = location;
		this.friend = friend;

	}

	/**
	 * Serialize Trip object by using writeToParcel. 
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
		dest.writeString(this.date);
		dest.writeString(this.location);
		dest.writeParcelable(this.friend, 0);

	}
	
	/**
	 * Feel free to add additional functions as necessary below.
	 */
	
	/**
	 * Do not implement
	 */
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}

	public String getName(){
		return this.name;
	}

	public String getDate(){
		return this.date;
	}

	public String getLocation(){
		return this.location;
	}

	public Person getFriend(){
		return this.friend;
	}

	public String toString(){
		return this.name + " ---- " + this.date;
	}
}
