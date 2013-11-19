/*
 * project		GoDisciple
 * 
 * package		com.lucyhutcheson.libs
 * 
 * @author		Lucy Hutcheson
 * 
 * date			Nov 16, 2013
 * 
 */
package com.lucyhutcheson.libs;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// SETUP OUR VARIABLES
	static final String TAG = "DatabaseHandler";
	private static final String DATABASE_NAME = "godisciple.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "disciple";
	private static final String KEY_ID = "id";
	private static final String KEY_FIRST = "first";
	private static final String KEY_LAST = "last";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_AGE = "age";
	private static final String KEY_TIMESTAMP = "timestamp";
	private static final String DATABASE_CREATE = "create table if not exists " + TABLE_NAME + " (" + KEY_ID + " integer primary key, " + KEY_FIRST + " text, " + KEY_LAST + " text, " + KEY_EMAIL + " text, " + KEY_PHONE + " text, " + KEY_AGE + " integer, " + KEY_TIMESTAMP + " integer)";
	private List<Disciple> discipleResult;
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
    	Log.i(TAG, "DB CREATED ");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	// RETURNS ALL DISCIPLES CURRENTLY SAVED IN LOCAL SQL DB
	public List<Disciple> getAllDisciples() {
    	Log.i(TAG, "getAllDisciples STARTED");
		List<Disciple> discipleList = new ArrayList<Disciple>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Disciple discipleObject = new Disciple();
				discipleObject.setID(Integer.parseInt(cursor.getString(0)));
				discipleObject.setFirst(cursor.getString(1));
				discipleObject.setLast(cursor.getString(2));
				discipleObject.setPhone(cursor.getString(3));
				discipleObject.setEmail(cursor.getString(4));
				discipleObject.setAge(cursor.getInt(5));
				
				discipleList.add(discipleObject);
			}
			while (cursor.moveToNext());
		}
		return discipleList;
	}
	
	// ADDS ONE SINGLE DISCIPLE TO THE LOCAL SQL DB
	public void addDisciple(Disciple discipleObject) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_FIRST, discipleObject.getFirst()); 
	    values.put(KEY_LAST, discipleObject.getLast()); 
	    values.put(KEY_EMAIL, discipleObject.getEmail()); 
	    values.put(KEY_PHONE, discipleObject.getPhone()); 
	    values.put(KEY_AGE, discipleObject.getAge()); 
	 
	    // INSERT QUERY
	    db.insert(TABLE_NAME, null, values);
	    db.close(); 
	}
	
	// RETURNS A SORTED LIST BASED ON FILTER SELECTED BY USER
	public List<Disciple> sortTable(String filter)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		discipleResult = null;
		discipleResult = new ArrayList<Disciple>();
		Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_FIRST, KEY_LAST, KEY_EMAIL, KEY_PHONE, KEY_AGE}, null, null, null, null, filter);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		if (cursor.moveToFirst()) {
			do {
				Disciple disciple = new Disciple(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
				discipleResult.add(disciple);
			} while (cursor.moveToNext());
		}

		// RETURN MY LIST
		return discipleResult;
	}
	
	// RETURNS THE COUNT OF DISCIPLES CURRENTLY SAVED IN LOCAL SQL DB
	public int getDiscipleCount() {
    	int count = 0;
		String query = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null) {
			count = cursor.getCount();
		}
		
		return count;
	}
	
	// DELETES THE SELECTED DISCIPLE FROM LOCAL SQL DB
	public boolean deleteDisciple(Disciple disciple) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(disciple.getID())  });
		db.close();
		return true;
	}
	
	// RETURNS ONE DISCPLE OBJECT BASED ON PASSED IN ID
	public Disciple getDisciple(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] {
                KEY_FIRST, KEY_LAST, KEY_EMAIL, KEY_PHONE, KEY_AGE }, KEY_ID + "=?",
                new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Disciple disciple = new Disciple(cursor.getString(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
		return disciple; 
	}
	
	
	// UPDATES DISCIPLE ENTRY BASED ON PASSED IN ID AND OBJECT DATA
	public int updateDisciple(Disciple disciple) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues newData = new ContentValues();
		newData.put(KEY_FIRST, disciple.getFirst());
		newData.put(KEY_LAST, disciple.getLast());
		newData.put(KEY_EMAIL, disciple.getEmail());
		newData.put(KEY_PHONE, disciple.getPhone());
		newData.put(KEY_AGE, disciple.getAge());
		
		Log.i(TAG, newData.toString());
		Log.i(TAG, String.valueOf(disciple.getID()));
		
		return db.update(TABLE_NAME, newData, KEY_ID + " = ?", new String[] { String.valueOf(disciple.getID()) });
		
	}
	
}
