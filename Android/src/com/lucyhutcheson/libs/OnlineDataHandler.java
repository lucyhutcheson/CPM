/*
 * project		GoDisciple
 * 
 * package		com.lucyhutcheson.libs
 * 
 * @author		Lucy Hutcheson
 * 
 * date			Nov 19, 2013
 * 
 */
package com.lucyhutcheson.libs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class OnlineDataHandler    {

	// SETUP VARIABLES
	static final String TAG = "ONLINEDATAHANDLER";
	ParseQuery<ParseObject> query;
	List<ParseObject> onlineList;
	DatabaseHandler localDB;
	ArrayList<HashMap<String, String>> queryResults;
	Context _context;
	
	
	public ArrayList<HashMap<String, String>> getAllOnline() {
    	Log.i(TAG, "getAllONLINEDisciples STARTED");

		onlineList = new ArrayList<ParseObject>();
		
		query = ParseQuery.getQuery("Disciple");
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				Log.i(TAG, objects.toString());

				long timeStamp = (int) (System.currentTimeMillis()/1000);
				queryResults = new ArrayList<HashMap<String, String>>();
				for (int i=0; i<objects.size(); i++) {
					String first = objects.get(i).get("first").toString();
					String last = objects.get(i).get("last").toString();
					String email = objects.get(i).get("email").toString();
					String phone = objects.get(i).get("phone").toString();
					String age = objects.get(i).get("age").toString();
					int localId = Integer.parseInt(objects.get(i).get("localId").toString());
					
					// ADD TO HASHMAP
					queryResults.add(createMap(localId, first,	last, age, email, phone, timeStamp));
				}
			}
		});
				
		return queryResults;
	}

	// CREATE HASHMAP FUNCTION
	public HashMap<String, String> createMap(int id, String first,
			String last, String age, String email, String phone, long time) {
		HashMap<String, String> discipleMap = new HashMap<String, String>();
		discipleMap.put("first", first);
		discipleMap.put("last", last);
		discipleMap.put("age", age);
		discipleMap.put("email", email);
		discipleMap.put("phone", phone);
		discipleMap.put("id", Integer.toString(id));
		discipleMap.put("timestamp", Long.toString(time));
		return discipleMap;
	}




	// ADDS ONE SINGLE DISCIPLE TO THE ONLINE DB
	public void addDisciple(Disciple myDisciple) {
				
		ParseObject newDisciple = new ParseObject("Disciple");
		newDisciple.put("localId", myDisciple.getID());
		newDisciple.put("first", myDisciple.getFirst());
		newDisciple.put("last", myDisciple.getLast());
		newDisciple.put("email", myDisciple.getEmail());
		newDisciple.put("phone", myDisciple.getPhone());
		newDisciple.put("age", myDisciple.getAge());
		newDisciple.saveInBackground();
	 
	}
	
	// DELETES THE SELECTED DISCIPLE FROM ONLINE DB
	public void deleteDisciple(Disciple disciple) {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Disciple");
		query.whereEqualTo("localId", disciple.getID());
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					(objects.get(0)).deleteInBackground();

		        } else {
		            Log.e(TAG, "Error: " + e.getMessage());
		        }				
			}
		});
	}

	// UPDATES DISCIPLE ENTRY BASED ON PASSED IN ID AND OBJECT DATA
	public void updateDisciple(OnlineDisciple discipleObject) {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Disciple");
		query.whereEqualTo("localId", discipleObject.getID());
		List<ParseObject> myList = null;
		try {
			myList = query.find();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String onlineID = myList.get(0).getString("objectId");
		
		final int id = discipleObject.getID();
		final String first = discipleObject.getFirst();
		final String last = discipleObject.getLast();
		final String email = discipleObject.getEmail();
		final String phone = discipleObject.getPhone();
		final int age = discipleObject.getAge();

		// GET MY OBJECT BY ID AND UPDATE IT
		query.getInBackground(onlineID, new GetCallback<ParseObject>() {
		  public void done(ParseObject newDisciple, ParseException e) {
		    if (e == null) {
				newDisciple.put("localId", id);
				newDisciple.put("first", first);
				newDisciple.put("last", last);
				newDisciple.put("email", email);
				newDisciple.put("phone", phone);
				newDisciple.put("age", age);
				newDisciple.saveInBackground();
		    }
		  }
		});
	}

	
}
