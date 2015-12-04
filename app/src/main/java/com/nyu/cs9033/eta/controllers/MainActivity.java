package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.R;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;


public class MainActivity extends FragmentActivity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// TODO - fill in here
		// Create tab views
		FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

		FragmentTabHost.TabSpec tabCurTrip = tabHost.newTabSpec("Cur Trip");
		FragmentTabHost.TabSpec tabUpComingTrips = tabHost.newTabSpec("Upcoming Trips");
		FragmentTabHost.TabSpec tabHistoryTrips = tabHost.newTabSpec("History Trips");


		// Set tab name and content
		tabCurTrip.setIndicator("Cur", null);
		tabUpComingTrips.setIndicator("Upcoming", null);
		tabHistoryTrips.setIndicator("History", null);

		// Add tabs to tab host for display
		tabHost.addTab(tabCurTrip, TabCurTripFragment.class, null);
		tabHost.addTab(tabUpComingTrips, TabUpcomingTripsFragment.class, null);
		tabHost.addTab(tabHistoryTrips, TabHistoryTripsFragment.class, null);
	}

	/**
	 * This method should start the
	 * Activity responsible for creating
	 * a Trip.
	 */
	public void startCreateTripActivity(View view) {

		// TODO - fill in here
		Intent intent = new Intent(this, CreateTripActivity.class);
		startActivity(intent);
	}

}
