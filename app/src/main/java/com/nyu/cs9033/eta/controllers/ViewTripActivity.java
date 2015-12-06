package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.dbHelpers.TripDatabaseHelper;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ViewTripActivity extends Activity {

	private static final String TAG = "ViewTripActivity";

	private long tripId;
	private String address;
	private String friends;
	private String time;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_view_trip);
		// TODO - fill in here
		
		initFields(getIntent());

		viewTrip();
	}

//	/**
//	 * Create a Trip object via the recent trip that
//	 * was passed to TripViewer via an Intent.
//	 *
//	 * @param i The Intent that contains
//	 * the most recent trip data.
//	 *
//	 * @return The Trip that was most recently
//	 * passed to TripViewer, or null if there
//	 * is none.
//	 */
//	public Trip getTrip(Intent i) {
//
//		// TODO - fill in here
//		Trip trip = i.getExtras().getParcelable("trip");
//		return trip;
//	}

	private void initFields(Intent i){
		tripId = i.getExtras().getLong("tripId");
		Log.i(TAG, "=======initFields::trip id is: " + tripId + "==========");
		address = i.getExtras().getString("address");

		friends = buildFriendList(tripId);
		Log.e(TAG, "=======initFields::friend list: " + friends + "===========");
		time = i.getExtras().getString("time");
	}

	private String buildFriendList(long tripId){
		StringBuilder builder = new StringBuilder();
		TripDatabaseHelper db = new TripDatabaseHelper(this);

		Cursor cursor = db.getPersonByTripId(tripId);
		while(cursor.moveToNext()){
			builder.append(cursor.getString(0));
			if (!cursor.isLast()){
				builder.append(',');
			}
		}
		return builder.toString();
	}

	/**
	 * Populate the View using a Trip model.
	 * 
	 * populate the View.
	 */
	public void viewTrip() {
		
		// TODO - fill in here

		TextView time = (TextView)findViewById(R.id.textView_cur_trip_time_value);
		TextView tripLocation = (TextView)findViewById(R.id.textView_cur_trip_location_value);
		TextView friends = (TextView)findViewById(R.id.textView_view_trip_friendName_value);

		time.setText(this.time);
		tripLocation.setText(this.address);
		friends.setText(this.friends);
	}

	/**
	 * Callback function for OK button on view trip view
	 * */
	public void onClickOK(View view){
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * Callback function for Active button on view trip view
	 * */
	public void onClickActive(View view){
		finish();
	}
}
