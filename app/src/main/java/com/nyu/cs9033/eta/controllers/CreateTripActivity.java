package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.dbHelpers.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.models.Location;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
	private String dateStr;

	private final int REQUEST_CONTACT = 1; // OPT code for request contact
	private final int SEARCH_LOC = 2; // OPT code for location search
	// URI for using HW3API location service
	private final Uri HW3API_LOC_URI = Uri.parse("location://com.example.nyu.hw3api");
	private final String LOC_PROVIDER = "FOURSQUARE";

	private List<Person> personList;
	private TripDatabaseHelper db;

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

	private String buildDateStr(int year, int month, int day){
		StringBuilder dateStr = new StringBuilder()
				.append(year).append("-");

		if(month < 10) dateStr.append("0");
		dateStr.append(month+1).append("-");

		if(day < 10) dateStr.append("0");
		dateStr.append(day);

		return dateStr.toString();
	}

	private String buildTimeStr(int hour, int min){
		StringBuilder timeStr = new StringBuilder();
		if(hour < 10) timeStr.append("0");
		timeStr.append(hour).append(":");

		if(min < 10) timeStr.append("0");
		timeStr.append(min);

		return timeStr.toString();
	}


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

		//init person list
		this.personList = new ArrayList<Person>();

		//init database helper
		db = new TripDatabaseHelper(this);
	}
	
	/**
	 * This method should be used to
	 * instantiate a Trip model object.
	 * 
	 * @return The Trip as represented
	 * by the View.
	 */
//	public void createTrip() {
	public boolean validateInput(){
		// TODO - fill in here

		String tripDate = tripDateText.getText().toString().trim();
		String tripTime = tripTimeText.getText().toString().trim();
		String tripDestination = tripDestinationText.getText().toString().trim();
		String tripFriends = tripFriendsText.getText().toString().trim();

		if(tripDestination == null || tripDestination.trim().length() == 0){
			tripDestinationText.setError("Trip name can not be empty");
			return false;
		}

		if(tripDate == null || tripDate.trim().length() == 0){
			tripDateText.setError("Trip date can not be empty");
			return false;
		}

		if(tripTime == null || tripTime.trim().length() == 0){
			tripTimeText.setError("Trip time can not be empty");
			return false;
		}

		if(tripFriends == null || tripFriends.trim().length() == 0){
			tripFriendsText.setError("Trip friend list can not be empty");
			return false;
		}

		this.dateStr = tripDate+" "+tripTime;

		return true;
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
	public int saveTrip(TripDatabaseHelper db, Trip trip, long tripId) {

		// TODO - fill in here

		/**
		 * This function will handle save trip object to database latter
		 * In current implementation it will always return true;
		 * */

		db.insertTrip(trip, tripId);

		return 1;
	}

	public void onClickSaveTrip(View view){

//		Intent intent = new Intent();



		boolean valid = validateInput();

		if(valid){
//			/* Save trip, will return save status */
//			boolean saveSuccess = saveTrip(newTrip);
//
//			if(saveSuccess){
//				finish();
//			}
			/**
			 *  use async task to create trip record on web service
			 *  also save data back to database
			 *  */
			String url = "http://cs9033-homework.appspot.com/";

			new CreateTripTask().execute(url);
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
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
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
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER
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
		String name = c.getString(0);
		String number = c.getString(1);

		if(friends.length() != 0){
			friends += ",";
		}
		friends += name;

		tripFriendsText.setText(friends);

		// add current person into person list
		personList.add(new Person(name, number));

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

	/**
	 * This function will send request and receive result from HW3API
	 * to get location information
	 * */
	public void onClickSearchLocation(View view){
		Intent searchLocationIntent = new Intent(
				Intent.ACTION_VIEW, HW3API_LOC_URI);

		String searchStr = tripDestinationText.getText().toString();
		if(searchStr != null && searchStr.length() > 0){
			searchLocationIntent.putExtra("searchVal", searchStr);
			startActivityForResult(searchLocationIntent, SEARCH_LOC);
		}
	}

	/**
	 * This function will save contact information from
	 * contact application to database
	 * @return person _id
	 * */
	private int savePerson(TripDatabaseHelper db, Person person){
		// first, search for existing person
		Cursor cursor = db.getPerson(person);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			return cursor.getInt(0);
		}
		// If no existing record, create new person
		return (int)db.insertPerson(person);
	}

	/**
	 * This function will save trip person map
	 * information to database
	 * */
	private int saveTripPersonMap(TripDatabaseHelper db, long tripId, int personId){
		return (int)db.insertTripPersonMap(tripId, personId);
	}


	/**
	 * This function will save location information
	 * to database
	 * @return location _id
	 */
	private int saveLocation(TripDatabaseHelper db, Location location){
		// first, search for existing location
		Cursor cursor = db.getLocation(location);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			return cursor.getInt(0);
		}

		// if no existing record, create new record
		return (int)db.insertLocation(location);
	}

	private String sendCreateTripRequest(String urlStr) throws IOException {
		String UserAgent = "Mozilla/5.0";
		String postBody = buildPostBody();
		Log.e(TAG, "=====sendCreateTripRequest::post body is: " + postBody + "========");
		StringBuilder response = new StringBuilder();

		URL url = new URL(urlStr);


		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		// may want to set timeout
		try{
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", UserAgent);

			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(postBody);

			out.flush();
			out.close();

			Scanner in = new Scanner(con.getInputStream());
			while(in.hasNext()){
				response.append(in.nextLine());
				Log.e(TAG, "======sendCreateTripRequest::response is: " + response + "===========");
			}
		}finally{
			con.disconnect();
		}
		return response.toString();
	}

	/**
	 * This method will help to build post body in json string format
	 * */
	private String buildPostBody(){
		JSONObject jsonObj = new JSONObject();
		String[] locInfo = null;
		DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm");
		Date date;

		try{
			jsonObj.put("command", "CREATE_TRIP");
			if(this.loc != null){
				locInfo = new String[]{loc.getName(), loc.getAddress(),
					Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude())};
				jsonObj.put("location", locInfo);
			}
			date = format.parse(this.dateStr);
			jsonObj.put("datetime", date.getTime()/1000);
			jsonObj.put("people", tripFriendsText.getText().toString().trim());

		}catch(JSONException je){
			je.printStackTrace();
			return null;
		}catch(ParseException pe){
			pe.printStackTrace();
			return null;
		}

		return jsonObj.toString();
	}

	/**
	 * private inner class CreateTripTask
	 * Will send POST request to web service
	 * */
	private class CreateTripTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls){
			String result = null;
			try{
				result = sendCreateTripRequest(urls[0]);
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result){

			JSONObject json;
			try{
				json = new JSONObject(result);
				int resCode = (Integer)json.get("response_code");
				if(resCode != 0){
					Log.e(TAG, "Error occur with HTTP request of creating trip");
					return;
				}
				// Get tripID from web service
				long tripId = (Long)json.get("trip_id");

				// Insert location
				int locId = saveLocation(db, loc);

				Trip trip = new Trip(dateStr, locId, false, false);
				// Insert trip
				saveTrip(db, trip, tripId);

				// for each person in the person array, a new person should be created
				for(Person p: personList){
					// Insert person
					int personId = savePerson(db, p);
					// Insert trip person map
					saveTripPersonMap(db, tripId, personId);
				}

				finish();

			}catch(JSONException je){
				je.printStackTrace();
				return;
			}

		}
	}

}

