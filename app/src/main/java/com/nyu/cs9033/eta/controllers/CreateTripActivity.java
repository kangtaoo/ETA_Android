package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateTripActivity extends Activity {
	
	private static final String TAG = "CreateTripActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_trip);
		// TODO - fill in here
	}
	
	/**
	 * This method should be used to
	 * instantiate a Trip model object.
	 * 
	 * @return The Trip as represented
	 * by the View.
	 */
	public Trip createTrip() {
	
		// TODO - fill in here
		EditText tripNameText = ((EditText)findViewById(R.id.editText_new_trip_name));

		EditText tripDateText = ((EditText)findViewById(R.id.editText_new_trip_date));
		EditText tripTimeText = ((EditText)findViewById(R.id.editText_new_trip_time));
		EditText tripLocationText = (EditText)findViewById(R.id.editText_new_trip_location);
		EditText tripFriendNameText = ((EditText)findViewById(R.id.editText_new_trip_friend_name));
//		EditText tripFriendLocationText = ((EditText)findViewById(R.id.editText_new_trip_friend_location));

		String tripName = tripNameText.getText().toString().trim();
		String tripDate = tripDateText.getText().toString().trim();
		String tripTime = tripTimeText.getText().toString().trim();
		String tripLocation = tripLocationText.getText().toString().trim();
		String tripFriendName = tripFriendNameText.getText().toString().trim();
//		String tripFriendLocation = tripFriendLocationText.getText().toString().trim();

		if(tripName == null || tripName.trim().length() == 0){
			tripNameText.setError("Trip name can not be empty");
			return null;
		}

		if(tripDate == null || tripDate.trim().length() == 0){
			tripDateText.setError("Trip date can not be empty");
			return null;
		}

		if(tripTime == null || tripTime.trim().length() == 0){
			tripTimeText.setError("Trip time can not be empty");
			return null;
		}

		if(tripLocation == null || tripLocation.trim().length() == 0){
			tripLocationText.setError("Trip location can not be empty");
			return null;
		}

		if(tripFriendName == null || tripFriendName.trim().length() == 0){
			tripFriendNameText.setError("Trip friend name can not be empty");
			return null;
		}

		/*
		if(tripFriendLocation == null || tripFriendLocation.trim().length() == 0){
			tripFriendLocationText.setError("Trip friend location can not be empty");
			return null;
		}
		*/
		Person friend = new Person(tripFriendName);

		Trip newTrip = new Trip(tripName, tripDate, tripTime, tripLocation, friend);
		return newTrip;
	}

	/**
	 * For HW2 you should treat this method as a 
	 * way of sending the Trip data back to the
	 * main Activity.
	 * 
	 * Note: If you call finish() here the Activity 
	 * will end and pass an Intent back to the
	 * previous Activity using setResult().
	 * 
	 * @return whether the Trip was successfully 
	 * saved.
	 */
	public boolean saveTrip(Trip trip) {

		// TODO - fill in here

		/**
		 * This function will handle save trip object to database latter
		 * In current implementation it will always return true;
		 * */

		return true;
	}

	public void onClickSaveTrip(View view){

		Intent intent = new Intent();

		Trip newTrip = createTrip();

		if(newTrip != null){
			/* Save trip, will return save status */
			boolean saveSuccess = saveTrip(newTrip);

			if(saveSuccess){

				intent.putExtra("trip", newTrip);

				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}

	/**
	 * This method should be used when a
	 * user wants to cancel the creation of
	 * a Trip.
	 * 
	 * Note: You most likely want to call this
	 * if your activity dies during the process
	 * of a trip creation or if a cancel/back
	 * button event occurs. Should return to
	 * the previous activity without a result
	 * using finish() and setResult().
	 */
	public void cancelTrip(View view) {
	
		// TODO - fill in here
		setResult(RESULT_CANCELED);
		finish();
	}
}
