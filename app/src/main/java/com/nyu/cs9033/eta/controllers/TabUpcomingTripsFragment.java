package com.nyu.cs9033.eta.controllers;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.dbHelpers.TripDatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabUpcomingTripsFragment extends Fragment {


    public TabUpcomingTripsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_upcoming_trips, container, false);

        ListView listView = (ListView)view.findViewById(R.id.listView_upcoming_trips);

        TripDatabaseHelper dbHelper = new TripDatabaseHelper(getActivity());
        Cursor cursor = dbHelper.getUpcomingTrips();

        String[] dbColumns = {
                "destination",
                "time"
        };

        int[] viewListItems = {
                R.id.textView_trip_list_item_loc,
                R.id.textView_trip_list_item_time
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.layout_trip_list_item,
                cursor,
                dbColumns,
                viewListItems,
                0
        );

        listView.setAdapter(adapter);

        return view;


    }


}
