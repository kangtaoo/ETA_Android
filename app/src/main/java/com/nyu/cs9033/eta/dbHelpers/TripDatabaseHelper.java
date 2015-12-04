package com.nyu.cs9033.eta.dbHelpers;
import com.nyu.cs9033.eta.models.Location;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.*;


/**
 * Created by kangkang on 10/22/15.
 */
public class TripDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "trips";
    private static final String TABLE_TRIP = "trip";
    private static final String COLUMN_TRIP_ID = "_id";
//    private static final String COLUMN_TRIP_DATE = "date";
    private static final String COLUMN_TRIP_TIME = "time";
    private static final String COLUMN_TRIP_LOCATION_ID = "location_id";
    private static final String COLUMN_TRIP_ACTIVE = "active";
    private static final String COLUMN_TRIP_ARRIVED = "arrived";


    private static final String TABLE_LOCATION = "location";

    private static final String COLUMN_LOC_ID = "_id";
    private static final String COLUMN_LOC_LAT = "latitude";
    private static final String COLUMN_LOC_LONG = "longitude";
    private static final String COLUMN_LOC_NAME = "name";
    private static final String COLUMN_LOC_ADDRESS = "address";

    private static final String TABLE_PERSON = "person";
    private static final String COLUMN_PERSON_ID = "_id";
    private static final String COLUMN_PERSON_NAME = "name";
    private static final String COLUMN_PERSON_PHONENUM = "phone";

    private static final String TABLE_TRIP_PERSON_MAP = "trip_person_map";
    private static final String COLUMN_TPM_ID = "_id";
    private static final String COLUMN_TPM_TRIPID = "trip_id";
    private static final String COLUMN_TPM_PERSONID = "person_id";


    public TripDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // SQL script used to create trip table
        String createTripTable = "CREATE TABLE " + TABLE_TRIP + " ("
                + COLUMN_TRIP_ID + " INTEGER primary key, "
                + COLUMN_TRIP_LOCATION_ID + " REFERENCES location(_id), "
                + COLUMN_TRIP_TIME + " TEXT, "
                + COLUMN_TRIP_ACTIVE + " INTEGER, "
                + COLUMN_TRIP_ARRIVED + " INTEGER)";

        // Create trip table
        db.execSQL(createTripTable);

        // SQL script used to create location table
        String createLocTable = "CREATE TABLE " + TABLE_LOCATION + " ("
                + COLUMN_LOC_ID + " INTEGER primary key AUTOINCREMENT, "
                + COLUMN_LOC_LAT + " REAL, "
                + COLUMN_LOC_LONG + " REAL, "
                + COLUMN_LOC_NAME + " TEXT, "
                + COLUMN_LOC_ADDRESS + " TEXT)";

        // Create location table
        db.execSQL(createLocTable);

        // SQL script used to create person table
        String createPersonTable = "CREATE TABLE " + TABLE_PERSON + " ("
                + COLUMN_PERSON_ID + " INTEGER primary key AUTOINCREMENT, "
                + COLUMN_PERSON_NAME + " TEXT, "
                + COLUMN_PERSON_PHONENUM + " INTEGER)";
        // Create person table
        db.execSQL(createPersonTable);

        // SQL script used to create trip-person-map table
        String createTripPersonMapTable = "CREATE TABLE " + TABLE_TRIP_PERSON_MAP + " ("
                + COLUMN_TPM_ID + " INTEGER primary key AUTOINCREMENT, "
                + COLUMN_TPM_TRIPID + " INTEGER references trip(_id), "
                + COLUMN_TPM_PERSONID + " INTEGER references person(_id))";
        // Create trip-person-map table
        db.execSQL(createTripPersonMapTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersionNum, int newVersionNum){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_PERSON_MAP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);

        onCreate(db);
    }

    public long insertTrip(Trip trip, long locId){
        ContentValues cv = new ContentValues();
        // Needs to get trip time information
        cv.put(COLUMN_TRIP_TIME, trip.getTime());
        // Add location id into current trip
        cv.put(COLUMN_TRIP_LOCATION_ID, locId);
        // Needs to get trip destination
        cv.put(COLUMN_TRIP_ACTIVE, trip.isActive()?1:0);
        // Add friend lists for current trip
        cv.put(COLUMN_TRIP_ARRIVED, trip.isArrived()?1:0);

        // return id of new trip
        return getWritableDatabase().insert(TABLE_TRIP, null, cv);
    }

    /**
     * @return search result with given latitude and longitude
     * */
    public Cursor getLocation(double latitude, double longitude){
        return null;
    }

    public long insertLocation(Location location){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOC_NAME, location.getName());
        cv.put(COLUMN_LOC_ADDRESS, location.getAddress());
        cv.put(COLUMN_LOC_LAT, location.getLatitude());
        cv.put(COLUMN_LOC_LONG, location.getLongitude());

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
                COLUMN_TRIP_TIME + ") < strftime('%s', 'now') ORDER BY " +
                COLUMN_TRIP_TIME + " ASC";
        Cursor cursor = db.rawQuery(queryStr, null);

        return cursor;
    }

    public Cursor getUpcomingTrips(){
        SQLiteDatabase db = this.getReadableDatabase();
        /*query for upcoming trip goes here*/

        String queryStr;

        Cursor cur = this.getCurTrip();

        if(cur.getCount() == 0){
            queryStr = "SELECT * FROM " + TABLE_TRIP + " WHERE strftime('%s', "+
                    COLUMN_TRIP_TIME + ") > strftime('%s', 'now') ORDER BY " +
                    COLUMN_TRIP_TIME + " ASC";
        }else{
            queryStr = "SELECT * FROM " + TABLE_TRIP + " WHERE strftime('%s', "+
                    COLUMN_TRIP_TIME + ") > strftime('%s', 'now') ORDER BY " +
                    COLUMN_TRIP_TIME + " ASC LIMIT " + Integer.MAX_VALUE +
                    " OFFSET 1";
        }

        Cursor cursor = db.rawQuery(queryStr, null);

        return cursor;
    }

    public Cursor getCurTrip(){

        SQLiteDatabase db = this.getReadableDatabase();
        /*query for current trip goes here*/
        String queryStr = "SELECT * FROM trip WHERE strftime('%s', " +
                COLUMN_TRIP_TIME + ") > strftime('%s', 'now') AND " +
                "date(" + COLUMN_TRIP_TIME + ") = date('now') " +
                "ORDER BY " + COLUMN_TRIP_TIME + " LIMIT 1;" ;
        Cursor cursor = db.rawQuery(queryStr, null);

        return cursor;
    }

    private String getCurrentTime(){
        String result = null;
        Calendar cal = Calendar.getInstance();


        return result;
    }

    /**
     * Search person from database via name and number
     * */
    public Cursor getPerson(String name, String number){
        return null;
    }

    public long insertPerson(Person person){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PERSON_NAME, person.getName());
        cv.put(COLUMN_PERSON_PHONENUM, person.getNumber());

        return getWritableDatabase().insert(TABLE_PERSON, null, cv);
    }

    public long insertTripPersonMap(long tripID, int personID ){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TPM_TRIPID, tripID);
        cv.put(COLUMN_TPM_PERSONID, personID);

        return getWritableDatabase().insert(TABLE_TRIP_PERSON_MAP, null, cv);
    }
}

