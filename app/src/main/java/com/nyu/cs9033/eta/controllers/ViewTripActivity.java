package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

public class ViewTripActivity extends Activity {

	private static final String TAG = "ViewTripActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_view_trip);

		// TODO - fill in here
		
		Trip trip = getTrip(getIntent());
		viewTrip(trip);
	}
	
	/**
	 * Create a Trip object via the recent trip that
	 * was passed to TripViewer via an Intent.
	 * 
	 * @param i The Intent that contains
	 * the most recent trip data.
	 * 
	 * @return The Trip that was most recently
	 * passed to TripViewer, or null if there
	 * is none.
	 */
	public Trip getTrip(Intent i) {
		
		// TODO - fill in here
		Trip trip = i.getExtras().getParcelable("trip");
		return trip;
	}

	/**
	 * Populate the View using a Trip model.
	 * 
	 * @param trip The Trip model used to
	 * populate the View.
	 */
	public void viewTrip(Trip trip) {
		
		// TODO - fill in here
		/*TextView tripName = (TextView)findViewById(R.id.textView_view_trip_name_value);
		TextView tripDate = (TextView)findViewById(R.id.textView_view_trip_date_value);
		TextView tripLocation = (TextView)findViewById(R.id.textView_view_trip_location_value);
		TextView friendName = (TextView)findViewById(R.id.textView_view_trip_friendName_value);
		TextView friendLocation = (TextView)findViewById(R.id.textView_view_trip_friendLocation_value);

		tripName.setText(trip.getName());
		tripDate.setText(trip.getDate());
		tripLocation.setText(trip.getLocation());
		friendName.setText(trip.getFriend().getName());
		friendLocation.setText(trip.getFriend().getLocation());*/
	}

	/**
	 * Callback function for OK button on view trip view
	 * */
	public void onClickOK(View view){
		setResult(RESULT_CANCELED);
		finish();
	}
}
