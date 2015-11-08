package com.nyu.cs9033.eta.controllers;

//import android.app.Activity;
//import android.net.Uri;
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
import com.nyu.cs9033.eta.models.Trip;

public class TabCurTripFragment extends Fragment {


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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_cur_trip, container, false);
        ListView listView = (ListView)view.findViewById(R.id.listView_cur_trip_detail);

        TripDatabaseHelper dbHelper = new TripDatabaseHelper(getActivity());
        Cursor cursor = dbHelper.getCurTrip();

        String[] dbColumns = {
                "destination",
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

        listView.setAdapter(adapter);

        return view;
    }
}
