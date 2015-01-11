package com.example.volcanoalarm.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.volcanoalarm.bean.RelaxDate;
import com.example.volcanoalarm.bean.AlarmDate;
import com.example.volcanoalarm.util.DateUtil;

public class VolcanoDatabase {
    private static final String TAG = "VolcanoDatabase";

    private static final String DATABASE_NAME = "VolcanoDatabase.db";
    private static final int DATABASE_VERSION = 2;

    private final WildFishingOpenHelper mDatabaseOpenHelper;

    /**
     * Constructor
     * @param context The Context within which to work, used to create the DB
     */
    public VolcanoDatabase(Context context) {
        mDatabaseOpenHelper = new WildFishingOpenHelper(context);
    }
    
	public long addRelaxDate(RelaxDate rd) {
		// Gets the data repository in write mode
    	SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(VolcanoContract.RelaxDate.COLUMN_NAME_DATE, rd.getDate());

    	// Insert the new row, returning the primary key value of the new row
    	long newRowId;
    	newRowId = db.insert(
    			 VolcanoContract.RelaxDate.TABLE_NAME,
    	         null,
    	         values);
    	
    	return newRowId;
	}

	public int deleteRelaxDate(String date) {
    	SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
    	
    	String selection = VolcanoContract.RelaxDate.COLUMN_NAME_DATE + " = ? ";
		String[] selelectionArgs = { date };

    	int count = db.delete(
    			 VolcanoContract.RelaxDate.TABLE_NAME,
    			 selection,
    			 selelectionArgs);
    	
    	return count;
	}

	public List<RelaxDate> getAllRelaxDates() {
		SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
		String[] projection = { VolcanoContract.RelaxDate._ID,
				VolcanoContract.RelaxDate.COLUMN_NAME_DATE};

		Cursor c = db.query(VolcanoContract.RelaxDate.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		List<RelaxDate> list = new ArrayList<RelaxDate>();
		c.moveToPosition(-1);
		while(c.moveToNext()){
			RelaxDate rd = new RelaxDate();
			rd.setId(c.getString(c.getColumnIndex(VolcanoContract.RelaxDate._ID)));
			rd.setDate(c.getString(c.getColumnIndex(VolcanoContract.RelaxDate.COLUMN_NAME_DATE)));
			list.add(rd);
		}
		
		return list;
	}
	
	public boolean isSettedRelaxDate(String date) {
		SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
		String[] projection = { VolcanoContract.RelaxDate._ID,
				VolcanoContract.RelaxDate.COLUMN_NAME_DATE};
		
		String selection = VolcanoContract.RelaxDate.COLUMN_NAME_DATE + " = ? ";
		String[] selelectionArgs = { date };
		

		Cursor c = db.query(VolcanoContract.RelaxDate.TABLE_NAME, // The table to query
				projection, // The columns to return
				selection, // The columns for the WHERE clause
				selelectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c.getCount() > 0;
	}
	
	public long addAlarmDate(AlarmDate ad) {
		// Gets the data repository in write mode
    	SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(VolcanoContract.AlarmDate.COLUMN_NAME_DATE, ad.getDate());

    	// Insert the new row, returning the primary key value of the new row
    	long newRowId;
    	newRowId = db.insert(
    			 VolcanoContract.AlarmDate.TABLE_NAME,
    	         null,
    	         values);
    	
    	return newRowId;
	}

	public int deleteAlarmDate(String date) {
    	SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
    	
    	String selection = VolcanoContract.AlarmDate.COLUMN_NAME_DATE + " = ? ";
		String[] selelectionArgs = { date };

    	int count = db.delete(
    			 VolcanoContract.AlarmDate.TABLE_NAME,
    			 selection,
    			 selelectionArgs);
    	
    	return count;
	}

	public List<AlarmDate> getAvalibleAlarmDates() {
		SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
		String[] projection = { VolcanoContract.AlarmDate._ID,
				VolcanoContract.AlarmDate.COLUMN_NAME_DATE};
		
		String selection = VolcanoContract.AlarmDate.COLUMN_NAME_DATE + " >= ? ";
		String[] selelectionArgs = { DateUtil.getToday() };
		String order = VolcanoContract.AlarmDate.COLUMN_NAME_DATE + " ASC ";
		

		Cursor c = db.query(VolcanoContract.AlarmDate.TABLE_NAME, // The table to query
				projection, // The columns to return
				selection, // The columns for the WHERE clause
				selelectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				order // The sort order
				);

		List<AlarmDate> list = new ArrayList<AlarmDate>();
		c.moveToPosition(-1);
		while(c.moveToNext()){
			AlarmDate ad = new AlarmDate();
			ad.setId(c.getString(c.getColumnIndex(VolcanoContract.AlarmDate._ID)));
			ad.setDate(c.getString(c.getColumnIndex(VolcanoContract.AlarmDate.COLUMN_NAME_DATE)));
			list.add(ad);
		}
		
		return list;
	}
	
	public List<AlarmDate> getAllAlarmDates() {
		SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
		String[] projection = { VolcanoContract.AlarmDate._ID,
				VolcanoContract.AlarmDate.COLUMN_NAME_DATE};
		

		Cursor c = db.query(VolcanoContract.AlarmDate.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		List<AlarmDate> list = new ArrayList<AlarmDate>();
		c.moveToPosition(-1);
		while(c.moveToNext()){
			AlarmDate ad = new AlarmDate();
			ad.setId(c.getString(c.getColumnIndex(VolcanoContract.AlarmDate._ID)));
			ad.setDate(c.getString(c.getColumnIndex(VolcanoContract.AlarmDate.COLUMN_NAME_DATE)));
			list.add(ad);
		}
		
		return list;
	}
	
	public boolean isSettedAlarmDate(String date) {
		SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
		String[] projection = { VolcanoContract.AlarmDate._ID,
				VolcanoContract.AlarmDate.COLUMN_NAME_DATE};
		
		String selection = VolcanoContract.AlarmDate.COLUMN_NAME_DATE + " = ? ";
		String[] selelectionArgs = { date };
		

		Cursor c = db.query(VolcanoContract.AlarmDate.TABLE_NAME, // The table to query
				projection, // The columns to return
				selection, // The columns for the WHERE clause
				selelectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c.getCount() > 0;
	}
	

    /**
     * This creates/opens the database.
     */
    private static class WildFishingOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        
        private static final String SQL_CREATE_RELAX_DATE =
            "CREATE TABLE " + VolcanoContract.RelaxDate.TABLE_NAME + " (" +
            		VolcanoContract.RelaxDate._ID + " INTEGER PRIMARY KEY," +
            		VolcanoContract.RelaxDate.COLUMN_NAME_DATE + TEXT_TYPE +  
            " )"; 

        private static final String SQL_DELETE_RELAX_DATE =
            "DROP TABLE IF EXISTS " + VolcanoContract.RelaxDate.TABLE_NAME;
        
        private static final String SQL_CREATE_ALARM_DATE =
                "CREATE TABLE " + VolcanoContract.AlarmDate.TABLE_NAME + " (" +
                		VolcanoContract.AlarmDate._ID + " INTEGER PRIMARY KEY," +
                		VolcanoContract.AlarmDate.COLUMN_NAME_DATE + TEXT_TYPE +  
                " )"; 

            private static final String SQL_DELETE_ALARM_DATE =
                "DROP TABLE IF EXISTS " + VolcanoContract.AlarmDate.TABLE_NAME;
        
        

        WildFishingOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(SQL_CREATE_RELAX_DATE);
            mDatabase.execSQL(SQL_CREATE_ALARM_DATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL(SQL_DELETE_RELAX_DATE);
            db.execSQL(SQL_DELETE_ALARM_DATE);
            onCreate(db);
        }
    }

}
