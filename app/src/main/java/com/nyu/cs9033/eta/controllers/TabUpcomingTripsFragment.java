package com.nyu.cs9033.eta.controllers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;

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
        TextView textView = (TextView)view.findViewById(R.id.textView_upcoming_trips_hello);
        textView.setText("Hello, upcoming trips list goes here");
        return view;
    }


}
