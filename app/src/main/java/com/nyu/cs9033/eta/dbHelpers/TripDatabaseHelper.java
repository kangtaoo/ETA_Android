package com.nyu.cs9033.eta.dbHelpers;
import com.nyu.cs9033.eta.models.Trip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.util.*;


/**
 * Created by kangkang on 10/22/15.
 */
public class TripDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trips";
    private static final String TABLE_TRIP = "trip";
    private static final String COLUMN_TRIP_ID = "_id";
//    private static final String COLUMN_TRIP_DATE = "date";
    private static final String COLUMN_TRIP_TIME = "time";
    private static final String COLUMN_TRIP_DESTINATION = "destination";
    private static final String COLUMN_TRIP_FRIENDS = "friends";

    private static final String TABLE_LOCATION = "location";
    private static final String COLUMN_LOC_TRIPID = "trip_id";
    private static final String COLUMN_LOC_TIMESTAMP = "timestamp";
    private static final String COLUMN_LOC_LAT = "latitude";
    private static final String COLUMN_LOC_LONG = "longitude";
    private static final String COLUMN_LOC_ATI = "altitude";
    private static final String COLUMN_LOC_PROVIDER = "provider";


    public TripDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // SQL script used to create trip table
        String createTripTable = "CREATE TABLE " + TABLE_TRIP + " ("
                + COLUMN_TRIP_ID + " INTEGER primary key autoincrement, "
                + COLUMN_TRIP_TIME + " TEXT, "
                + COLUMN_TRIP_DESTINATION + " TEXT, "
                + COLUMN_TRIP_FRIENDS + " TEXT)";

        // Create trip table
        db.execSQL(createTripTable);

        // SQL script used to create location table
        String createLocTable = "CREATE TABLE " + TABLE_LOCATION + " ("
                + COLUMN_LOC_TRIPID + " INTEGER references trip(_id), "
                + COLUMN_LOC_LAT + " REAL, "
                + COLUMN_LOC_LONG + " REAL, "
                + COLUMN_LOC_ATI + " REAL, "
                + COLUMN_LOC_TIMESTAMP + " INTEGER, "
                + COLUMN_LOC_PROVIDER + " TEXT)";

        // Create person table
        db.execSQL(createLocTable);

        // SQL script used to create person table
        // Create person table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersionNum, int newVersionNum){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);

        onCreate(db);
    }

    public long insertTrip(Trip trip){
        ContentValues cv = new ContentValues();
        // Needs to get trip time information
        cv.put(COLUMN_TRIP_TIME, trip.getTime());
        // Needs to get trip destination
        cv.put(COLUMN_TRIP_DESTINATION, trip.getDestination());

        // return id of new trip
        return getWritableDatabase().insert(TABLE_TRIP, null, cv);
    }

    public long insertLocation(long tripID, Location location){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOC_TRIPID, tripID);
        cv.put(COLUMN_LOC_LAT, location.getLatitude());
        cv.put(COLUMN_LOC_LONG, location.getLongitude());
//        cv.put(COLUMN_LOC_ATI, location.getAltitude());
//        cv.put(COLUMN_LOC_PROVIDER, location.getProvider());
//        cv.put(COLUMN_LOC_TIMESTAMP, location.getTime());

        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }

    public Cursor getAllTrips(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRIP + " ORDER BY TIME ASC", null);

        return cursor;
    }

    public Cursor getHistoryTrips(){
        SQLiteDatabase db = this.getReadableDatabase();
        /*query for history trip goes here*/
        String queryStr = "SELECT * FROM " + TABLE_TRIP + " WHERE strftime('%s', "+
                COLUMN_TRIP_TIME + ") < strftime('%s', 'now')";
        Cursor cursor = db.rawQuery(queryStr, null);

        return cursor;
    }

    public Cursor getUpcomingTrips(){
        SQLiteDatabase db = this.getReadableDatabase();
        /*query for upcoming trip goes here*/
        String queryStr = "SELECT * FROM " + TABLE_TRIP + " WHERE strftime('%s', "+
                COLUMN_TRIP_TIME + ") > strftime('%s', 'now')";
        Cursor cursor = db.rawQuery(queryStr, null);

        return cursor;
    }

    public Cursor getCurTrip(){
        SQLiteDatabase db = this.getReadableDatabase();
        /*query for current trip goes here*/
        String queryStr = "" ;
        Cursor cursor = db.rawQuery(queryStr, null);

        return cursor;
    }

    private String getCurrentTime(){
        String result = null;
        Calendar cal = Calendar.getInstance();


        return result;
    }

}

