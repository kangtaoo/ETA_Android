package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable {
	
	// Member fields should exist here, what else do you need for a trip?
	// Please add additional fields
//	private String name;
//	private String date;
	private String time;
	private String destination;
	private String friends;
	
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
//		this.name = p.readString();
//		this.date = p.readString();
		this.time = p.readString();
		this.destination = p.readString();
		this.friends = p.readString();
//		this.friends = p.readParcelable(Person.class.getClassLoader());
	}

	/**
	 * Create a empty Trip model object
	 * This constructor will do nothing, leave all field be initial
	 * to default initial value.
	 * Need to call corresponding set***() function to the set value
	 * for corresponding fields.
	 */
	public Trip(){
		super();
	}
	
	/**
	 * Create a Trip model object from arguments
	 * 
	 * @param time, destination, friends
	 * Add arbitrary number of arguments to
	 * instantiate Trip class based on member variables.
	 */
	public Trip(String time, String destination, String friends) {
		
		// TODO - fill in here, please note you must have more arguments here
//		this.name = name;
//		this.date = date;
		this.time = time;
		this.destination = destination;
		this.friends = friends;

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
		dest.writeString(this.time);
		dest.writeString(this.destination);
		dest.writeString(this.friends);

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


	public String getTime(){
		return this.time;
	}

	public String getDestination(){
		return this.destination;
	}

	public String getFriend(){
		return this.friends;
	}

	public void setDestination(String destination){
		this.destination = destination;
	}

	public void setTime(String time){
		this.time = time;
	}

	public void setFriends(String friends){
		this.friends = friends;
	}

}
