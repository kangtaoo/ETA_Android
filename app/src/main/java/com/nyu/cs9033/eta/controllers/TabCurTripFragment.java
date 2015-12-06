package com.nyu.cs9033.eta.controllers;

//import android.app.Activity;
//import android.net.Uri;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.dbHelpers.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class TabCurTripFragment extends Fragment {

    private static final String TAG = "TabCurTripFragment";
    private long tripId;
    private Timer tripStatusTimer;
    private Timer locationTimer;
    private Location curLocation;
    private LocationManager locationManager;
    private String locProvider;


    public TabCurTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLocProvider();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TripDatabaseHelper db = new TripDatabaseHelper(getActivity());
        Cursor cursor = db.getCurTrip();

        /**
         * cursor result structure:
         * 0: trip ID (int)
         * 1. trip time
         * 2: location name
         * 3: location address
         * */

        // If there is no current trip, show nothing
        if(cursor.getCount() == 0){
            Log.i(TAG, "======onCreateView::No current trip is found, show nothing========");
            return null;
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_cur_trip, container, false);

        cursor.moveToFirst();

        // init field trip id
        this.tripId = cursor.getLong(0);

        TextView name = (TextView)view.findViewById(R.id.textView_cur_trip_name_value);
        name.setText(cursor.getString(2));

        TextView address = (TextView)view.findViewById(R.id.textView_cur_trip_location_value);
        address.setText(cursor.getString(3));

        TextView time = (TextView)view.findViewById(R.id.textView_cur_trip_time_value);
        time.setText(cursor.getString(1));

        /*ListView listView = (ListView)view.findViewById(R.id.listView_cur_trip_detail);

        TripDatabaseHelper dbHelper = new TripDatabaseHelper(getActivity());
        Cursor cursor = dbHelper.getCurTrip();

        String[] dbColumns = {
                "name",
                "address",
                "time",
                "friends"
        };

        int[] viewListItems = {
                R.id.textView_trip_list_item_detail_loc,
                R.id.textView_trip_list_item_detail_time,
                R.id.textView_trip_list_item_detail_friends
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.layout_trip_list_item_detail,
                cursor,
                dbColumns,
                viewListItems,
                0
        );

        listView.setAdapter(adapter);*/

        tripStatusTimer = new Timer();
        UpdateTripStatusTimerTask tripStatusTimerTask = new UpdateTripStatusTimerTask();
        tripStatusTimer.schedule(tripStatusTimerTask, 0, 5 * 1000);

        locationTimer = new Timer();
        UpdateLocationTimerTask locationTimerTask = new UpdateLocationTimerTask();
        locationTimer.schedule(locationTimerTask, 0, 5 * 1000);

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(tripStatusTimer != null){
            tripStatusTimer.cancel();
        }

        if(locationTimer != null){
            locationTimer.cancel();
        }
    }

    private String buildUpdateTripStatusPostBody(){
        JSONObject json = new JSONObject();
        try{
            json.put("command", "TRIP_STATUS");
            json.put("trip_id", this.tripId);
        }catch(JSONException je){
            je.printStackTrace();
            return null;
        }

        return json.toString();
    }

    private class UpdateLocationTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            String result = null;
            try{
                result = sendUpdateLocationRequest(urls[0]);
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
                if(json.getInt("response_code") != 0){
                    Log.e(TAG, "=======UpdateLocationTask::onPostExecute(): error occur with request========");
                }
            }catch(JSONException je){
                je.printStackTrace();
                return;
            }
        }
    }

    private class UpdateLocationTimerTask extends TimerTask {
        String urlStr = "http://cs9033-homework.appspot.com/";

        @Override
        public void run(){
            Log.e(TAG, "======UpdateLocationTimerTask::in location timer task=======");
            UpdateLocationTask task = new UpdateLocationTask();
            task.execute(urlStr);
        }
    }

    private String sendUpdateLocationRequest(String urlStr) throws IOException{
        String UserAgent = "Mozilla/5.0";
        String postBody = buildUpdateLocationPostBody();
        Log.e(TAG, "=====sendUpdateLocationRequest::post body is: " + postBody + "========");
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
                Log.i(TAG, "======sendUpdateLocationRequest::response is: " + response + "===========");
            }
        }finally{
            con.disconnect();
        }
        return response.toString();
    }
    /**
     * Build post body for update location request
     * */
    private String buildUpdateLocationPostBody(){
        JSONObject json = new JSONObject();
        curLocation = getCurPosition();
        try{
            json.put("command", "UPDATE_LOCATION");
            json.put("latitude", curLocation.getAltitude());
            json.put("longitude", curLocation.getLongitude());
            json.put("datetime", new Date().getTime());

        }catch(JSONException je){
            je.printStackTrace();
            return null;
        }

        return json.toString();
    }

    private class UpdateTripStatusTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            String result = null;
            try{
                result = sendUpdateTripStatusRequest(urls[0]);
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
            Log.i(TAG, "======UpdateTripStatusTask::doInBackground: get result: " + result + "======");
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            JSONObject json;
            JSONArray distances;
            JSONArray times;
            JSONArray people;

            try{
                json = new JSONObject(result);

                distances = (JSONArray)json.get("distance_left");
                times = (JSONArray)json.get("time_left");
                people = (JSONArray)json.get("people");

            }catch(JSONException je){
                je.printStackTrace();
                return;
            }

            // show information in list view
            List<HashMap<String, String>> list = buildList(distances, times, people);

            String[] from = {"people", "ETA"};
            int[] to = {android.R.id.text1, android.R.id.text2};

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
                    android.R.layout.simple_list_item_2, from, to);

            ListView listView = (ListView)getActivity().findViewById(
                    R.id.listView_cur_trip_person_list);

            listView.setAdapter(adapter);
        }
    }

    private class UpdateTripStatusTimerTask extends TimerTask {
        String urlStr = "http://cs9033-homework.appspot.com/";

        @Override
        public void run(){
            UpdateTripStatusTask task = new UpdateTripStatusTask();
            task.execute(urlStr);
        }
    }

    private List<HashMap<String, String>> buildList(JSONArray distance, JSONArray times, JSONArray people){
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try{
            for(int i = 0; i < distance.length(); i++){
                list.add(buildItem(distance.getDouble(i),
                        times.getInt(i), people.getString(i)));
            }
        }catch(JSONException je){
            je.printStackTrace();
        }

        return list;
    }

    private HashMap<String, String> buildItem(double distance,
                                              long time, String people){
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("ETA", "\tDistance left: " + distance + "\n\tTime left: " + time);
        item.put("people", people);

        return item;
    }
    private String sendUpdateTripStatusRequest(String urlStr) throws IOException {
        String UserAgent = "Mozilla/5.0";
        String postBody = buildUpdateTripStatusPostBody();
        Log.i(TAG, "=====sendUpdateTripStatusRequest::post body is: " + postBody + "========");
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
                Log.i(TAG, "======sendUpdateTripStatusRequest::response is: " + response + "===========");
            }
        }finally{
            con.disconnect();
        }
        return response.toString();
    }

    private Location getCurPosition(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "======getCurrentPosition::something wrong with permission, will return null for current location========");

            return null;
        }
        Log.i(TAG, "======getCurrentPosition::permission is OK, try to get current position========");

        return locationManager.getLastKnownLocation(locProvider);


    }

    private void initLocProvider(){
        this.locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isNetworkEnabled && !isGPSEnabled){
            Log.e(TAG, "======initLocProvider::Neither of network nor gps is enabled========");
            return;
        }else if(isNetworkEnabled){
            this.locProvider = LocationManager.NETWORK_PROVIDER;
        }else{
            this.locProvider = LocationManager.GPS_PROVIDER;
        }

        Log.i(TAG, "======initLocProvider::location provider is: " + locProvider +"========");
        this.curLocation = locationManager.getLastKnownLocation(locProvider);
        Log.i(TAG, "======initLocProvider::initial location is: " + curLocation + "========");

        // set the listener, update the location per 3 seconds(3*1000) automatically or moving more than 8 meters
        locationManager.requestLocationUpdates(locProvider, 3 * 1000, 8, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                curLocation = locationManager.getLastKnownLocation(provider);
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }
}
