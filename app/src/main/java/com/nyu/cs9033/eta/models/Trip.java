package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable {
	
	// Member fields should exist here, what else do you need for a trip?
	// Please add additional fields
	private String time;
	private int locId;
	// record trip's status, whether corresponding trip is active (current trip)
	boolean isActive;
	// record trip's status, whether current user is arrived
	boolean isArrived;

	boolean isFinished;

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
		this.locId = p.readInt();
		this.isActive = (p.readByte()==1);
		this.isArrived = (p.readByte() == 1);
		this.isFinished = (p.readByte() == 1);
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
	public Trip(String time, int locId, boolean isActive, boolean isArrived, boolean isFinished) {
		
		// TODO - fill in here, please note you must have more arguments here

		this.time = time;
		this.locId = locId;
		this.isActive = isActive;
		this.isArrived = isArrived;
		this.isFinished = isFinished;
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
		dest.writeInt(this.locId);
		dest.writeByte((byte)(isActive ? 1 : 0));
		dest.writeByte((byte)(isArrived?1:0));
		dest.writeByte((byte)(isFinished?1:0));
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

	public int getLocId(){
		return this.locId;
	}

	public boolean isActive(){
		return this.isActive;
	}

	public boolean isArrived(){
		return this.isArrived;
	}

	public boolean isFinished(){
		return this.isFinished;
	}

	public void setLocId(int id){
		this.locId = id;
	}

	public void setActvie(boolean b){
		this.isActive = b;
	}

	public void setArrived(boolean b){
		this.isArrived = b;
	}

	public void setFinished(boolean b) {
		this.isFinished = b;
	}

	public void setTime(String time){
		this.time = time;
	}

}
