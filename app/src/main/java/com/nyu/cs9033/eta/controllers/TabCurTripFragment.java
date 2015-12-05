package com.nyu.cs9033.eta.controllers;

//import android.app.Activity;
//import android.net.Uri;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.dbHelpers.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Trip;

public class TabCurTripFragment extends Fragment {

    private static final String TAG = "TabCurTripFragment";


    public TabCurTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Log.e(TAG, "======onCreateView::No current trip is found, show nothing========");
            return null;
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_cur_trip, container, false);

        cursor.moveToFirst();

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

        return view;
    }
}
