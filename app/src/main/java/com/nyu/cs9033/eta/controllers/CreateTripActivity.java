package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.dbHelpers.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.models.Location;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateTripActivity extends Activity {
	
	private static final String TAG = "CreateTripActivity";

	private int year;
	private int month;
	private int day;

	private TextView tripDateText;
	private TextView tripTimeText;
	private EditText tripDestinationText;
	private EditText tripFriendsText;
	private final Calendar calendar = Calendar.getInstance();
	private Location loc;

	private final int REQUEST_CONTACT = 1; // OPT code for request contact
	private final int SEARCH_LOC = 2; // OPT code for location search
	// URI for using HW3API location service
	private final Uri HW3API_LOC_URI = Uri.parse("location://com.example.nyu.hw3api");
	private final String LOC_PROVIDER = "FOURSQUARE";

	/**
	 * Get current date and initial both TextEdit and  DatePicker
	 */
	public void setCurrentDate(){

		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH);
		this.day = calendar.get(Calendar.DAY_OF_MONTH);

		String dateStr = buildDateStr(this.year, this.month, this.day);

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);

		String timeStr = buildTimeStr(hour, min);

		tripDateText.setText(dateStr);
		tripTimeText.setText(timeStr);
	}

	/**
	 * Show dialog of date picker as well as time picker
	 * */
	/*@Override
	public Dialog onCreateDialog(int id){
		switch(id){
			case DATE_PICKER_ID:
				return new DatePickerDialog(this, datePickerListener, year, month, day);
		}
		return null;
	}*/

	private String buildDateStr(int year, int month, int day){
		String dateStr = new StringBuilder()
				.append(month+1).append("-")
				.append(day).append("-")
				.append(year).toString();
		return dateStr;
	}

	private String buildTimeStr(int hour, int min){
		return hour+":"+min;
	}




	/*private DatePickerDialog.OnDateSetListener datePickerListener =
			new DatePickerDialog.OnDateSetListener(){
				public void onDateSet(DatePicker view, int year, int month, int day){
					String dateStr = buildDateStr(year, month, day);
					EditText tripDateText = (EditText)findViewById(R.id.editText_new_trip_date);
					tripDateText.setText(dateStr);
				}
			};*/

	/**
	 * Callback function for edit text of trip date
	 * */
	public void onDateTextClick(View view){
		DatePickerDialog dateDialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year,
										  int month, int day){
						String dateStr = buildDateStr(year, month, day);
						tripDateText.setText(dateStr);
					}
				}, year, month, day);

		dateDialog.show();
	}

	/**
	 * Callback function for edit text of trip date
	 * */
	public void onTimeTextClick(View view){
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		boolean is24 = true;

		TimePickerDialog timeDialog = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener(){
					@Override
					public void onTimeSet(TimePicker view, int hour, int min){
						String timeStr = buildTimeStr(hour, min);
						tripTimeText.setText(timeStr);
					}
				}, hour, min, is24);

		timeDialog.show();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_trip);
		// TODO - fill in here

		this.tripDateText = (TextView)findViewById(R.id.textView_new_trip_date_value);
		this.tripTimeText = (TextView)findViewById(R.id.textView_new_trip_time_value);
		this.tripDestinationText = (EditText)findViewById(R.id.editText_new_trip_destination);
		this.tripFriendsText = (EditText)findViewById(R.id.editText_new_trip_friends);

		setCurrentDate();
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

		String tripDate = tripDateText.getText().toString().trim();
		String tripTime = tripTimeText.getText().toString().trim();
		String tripDestination = tripDestinationText.getText().toString().trim();
		String tripFriends = tripFriendsText.getText().toString().trim();

		if(tripDestination == null || tripDestination.trim().length() == 0){
			tripDestinationText.setError("Trip name can not be empty");
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

		if(tripFriends == null || tripFriends.trim().length() == 0){
			tripFriendsText.setError("Trip friend list can not be empty");
			return null;
		}

		Trip newTrip = new Trip(tripDate+" "+tripTime, tripDestination, tripFriends);
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

		TripDatabaseHelper db = new TripDatabaseHelper(this);
		db.insertTrip(trip);
		
		return true;
	}

	public void onClickSaveTrip(View view){

//		Intent intent = new Intent();

		Trip newTrip = createTrip();

		if(newTrip != null){
			/* Save trip, will return save status */
			boolean saveSuccess = saveTrip(newTrip);

			if(saveSuccess){

//				intent.putExtra("trip", newTrip);

//				setResult(RESULT_OK, intent);
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

	/**
	 * Callback function when click add friend
	 * */
	public void onClickAddFriend(View view){
		pickContact();
	}

	/**
	 * Open pick activity of contact application and then pass
	 * selected contact back to ETA application
	 * */

	private void pickContact(){
		Intent pickContactIntent = new Intent(
				Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(pickContactIntent, REQUEST_CONTACT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		// return directly if error occurs when try to pick contact
		if(resultCode != Activity.RESULT_OK &&
				resultCode != Activity.RESULT_FIRST_USER) return;

		switch(requestCode){
			case REQUEST_CONTACT:
				handlePickContactResult(data);
				break;
			case SEARCH_LOC:
				handleSearchLocResult(data);
				break;
		}
	}

	/**
	 *  Handle return data from pick contact activity
	 *  Will be called by onActivityResult()
	 *  */
	private void handlePickContactResult(Intent data){
		Uri contactUri = data.getData(); // get uri from Intent object rather than actual data

		// now query the ContentProvider with the data you want
		String[] queryFields = new String[]{
				ContactsContract.Contacts.DISPLAY_NAME
		};

		Cursor c = getContentResolver().query(contactUri, queryFields, null, null, null);

		// make sure the result is not empty
		if(c.getCount() == 0){
			c.close();
			return;
		}

		// Get origin test for tripFriendsText
		String friends = tripFriendsText.getText().toString();

		// Get the first row
		c.moveToFirst();
		String person = c.getString(0);

		if(friends.length() != 0){
			friends += ",";
		}
		friends += person;

		tripFriendsText.setText(friends);
		c.close();
	}

	/**
	 * Will handle return data from HW3API
	 * Will be called by onActivityResult()
	 * */
	private void handleSearchLocResult(Intent data){
		ArrayList<String> result = data.getExtras().getStringArrayList("retVal");
		loc = new Location();
		loc.setName(result.get(0));
		loc.setAddress(result.get(1));
		loc.setLatitude(Double.valueOf(result.get(2)));
		loc.setLongitude(Double.valueOf(result.get(3)));
		// set text to be <Location.name>-<Location.address>
		tripDestinationText.setText(result.get(0) + "-" + result.get(1));

	}

	public void onClickSearchLocation(View view){
		Intent searchLocationIntent = new Intent(
				Intent.ACTION_VIEW, HW3API_LOC_URI);

		String searchStr = tripDestinationText.getText().toString();
		if(searchStr != null && searchStr.length() > 0){
			searchLocationIntent.putExtra("searchVal", searchStr);
			startActivityForResult(searchLocationIntent, SEARCH_LOC);
		}
	}

}
