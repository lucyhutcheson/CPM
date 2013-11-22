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

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class OnlineDataHandler    {

	// SETUP VARIABLES
	static final String TAG = "ONLINEDATAHANDLER";
	ParseQuery<ParseObject> query;
	List<ParseObject> onlineList;
	DatabaseHandler localDB;

	
	
	public ArrayList<HashMap<String, String>> getAllOnline(boolean saveToLocal) {
    	Log.i(TAG, "getAllONLINEDisciples STARTED");

		ArrayList<HashMap<String, String>> queryResults = null;
		onlineList = new ArrayList<ParseObject>();
		
		query = ParseQuery.getQuery("Disciple");
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				Log.i(TAG, objects.toString());
				for (int i=0; i<objects.size(); i++) {
					onlineList.add(objects.get(i);
				}
			}
		});
				
		if (onlineList.size() > 0) {
			
			localDB.addAllDisciples(onlineList);
			
			/*queryResults = new ArrayList<HashMap<String, String>>();
			MainActivity mainAct = new MainActivity();
			
			for (ParseObject item : onlineList) {
				long timeStamp = (int) (System.currentTimeMillis()/1000);

				queryResults.add(mainAct.createMap((Integer)item.get("localId"), item.get("first").toString(),
						item.get("last").toString(), item.get("age").toString(),
						item.get("email").toString(), item.get("phone").toString(), timeStamp));
				
				if (saveToLocal) {
					addDisciplesToDB(item.get("first").toString(), item.get("last").toString(), item.get("email").toString(), item.get("phone").toString(), (Integer) item.get("age"));
				}
			}*/
		} 

		return queryResults;
	}

	// ADD OUR DISCIPLES TO LOCAL SQL DATABASE FOR SYNCING
	public void addDisciplesToDB(String first, String last, String email,
		String phone, int age) {
		Disciple myDisciple = new Disciple();
		myDisciple.setFirst(first);
		myDisciple.setLast(last);
		myDisciple.setEmail(email);
		myDisciple.setPhone(phone);
		myDisciple.setAge(age);

		// ALSO ADD THIS SETUP DATA TO OUR LOCAL SQL DB
		//DatabaseHandler localDB = new DatabaseHandler();
		//localDB.addDisciple(myDisciple);
	}


	// ADDS ONE SINGLE DISCIPLE TO THE ONLINE DB
	public void addDisciple(OnlineDisciple discipleObject) {
				
		ParseObject newDisciple = new ParseObject("Disciple");
		newDisciple.put("localId", discipleObject.getID());
		newDisciple.put("first", discipleObject.getFirst());
		newDisciple.put("last", discipleObject.getLast());
		newDisciple.put("email", discipleObject.getEmail());
		newDisciple.put("phone", discipleObject.getPhone());
		newDisciple.put("age", discipleObject.getAge());
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
