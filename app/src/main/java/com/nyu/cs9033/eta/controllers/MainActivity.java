package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.TabActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import java.util.*;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.TabHost;


public class MainActivity extends FragmentActivity {

	private static final String TAG = "MainActivity";
	private static final int CREATE_NEW_TRIP = 1001;
	private static final int VIEW_TRIP = 1002;

	private ArrayList<Trip> tripList = new ArrayList<Trip>();
	private ArrayAdapter<Trip> tripAdapter;
	/* This field will record currently selected trip object */
	private Trip curTrip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// TODO - fill in here
		// Create tab views
		FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//		tabHost.setup();

		System.out.println("=====================MainActivity::onCreate=====================");
		System.out.println("tabHost: " + tabHost);

		FragmentTabHost.TabSpec tabCurTrip = tabHost.newTabSpec("Cur Trip");
		FragmentTabHost.TabSpec tabUpComingTrips = tabHost.newTabSpec("Upcoming Trips");
		FragmentTabHost.TabSpec tabHistoryTrips = tabHost.newTabSpec("History Trips");

//		System.out.println("tabCurTrip: " + tabCurTrip);

		// Set tab name and content
		tabCurTrip.setIndicator("Cur", null);

		tabUpComingTrips.setIndicator("Upcoming", null);

		tabHistoryTrips.setIndicator("History", null);

//		tabHost.addTab();

		// Add tabs to tab host for display
		tabHost.addTab(tabCurTrip, TabCurTripFragment.class, null);
		tabHost.addTab(tabUpComingTrips, TabUpcomingTripsFragment.class, null);
		tabHost.addTab(tabHistoryTrips, TabHistoryTripsFragment.class, null);


		// Fill in content to list view
//		tripAdapter =
//				new ArrayAdapter<Trip>(this, android.R.layout.simple_list_item_1, tripList);
//		ListView tripListView = (ListView)findViewById(R.id.listView_main_trip_list);
//		tripListView.setAdapter(tripAdapter);
//
//		tripListView.setOnItemClickListener(listViewOnItemClickListener);

	}

	private OnItemClickListener listViewOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			curTrip = (Trip) parent.getItemAtPosition(position);
			startViewTripActivity(view);
		}
	};

	/**
	 * This method should start the
	 * Activity responsible for creating
	 * a Trip.
	 */
	public void startCreateTripActivity(View view) {

		// TODO - fill in here
		Intent intent = new Intent(this, CreateTripActivity.class);
		startActivity(intent);
//		startActivityForResult(intent, CREATE_NEW_TRIP);
	}
	
	/**
	 * This method should start the
	 * Activity responsible for viewing
	 * a Trip.
	 */
	public void startViewTripActivity(View view) {
		
		// TODO - fill in here
		Intent intent = new Intent(this, ViewTripActivity.class);
		intent.putExtra("trip", curTrip);
		startActivityForResult(intent, VIEW_TRIP);
	}
	
	/**
	 * Receive result from CreateTripActivity here.
	 * Can be used to save instance of Trip object
	 * which can be viewed in the ViewTripActivity.
	 * 
	 * Note: This method will be called when a Trip
	 * object is returned to the main activity. 
	 * Remember that the Trip will not be returned as
	 * a Trip object; it will be in the persisted
	 * Parcelable form. The actual Trip object should
	 * be created and saved in a variable for future
	 * use, i.e. to view the trip.
	 * 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO - fill in here
		if(requestCode == CREATE_NEW_TRIP){
			if(resultCode == RESULT_OK){
				Trip trip = data.getExtras().getParcelable("trip");
				this.tripAdapter.add(trip);
			}
		}
	}
}
