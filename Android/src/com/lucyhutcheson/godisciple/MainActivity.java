/*
 * project		GoDisciple
 * 
 * package		com.lucyhutcheson.godisciple
 * 
 * @author		Lucy Hutcheson
 * 
 * date			Nov 16, 2013
 * 
 */
package com.lucyhutcheson.godisciple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lucyhutcheson.libs.DatabaseHandler;
import com.lucyhutcheson.libs.Disciple;
import com.lucyhutcheson.libs.OnlineDataHandler;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity {

	// SETUP VARIABLES FOR CLASS
	Context context = this;
	ListView listView;
	DatabaseHandler db;
	OnlineDataHandler onlineDB;
	ArrayList<String> _disciples = new ArrayList<String>();
	ArrayList<HashMap<String, String>> discipleArrayList;
	String firstNames[] = new String[] { "Simon", "Andrew", "James", "John",
			"Philip", "Bartholomew", "Matthew", "Thaddaeus", "Simon", "Judas" };
	String lastNames[] = new String[] { "Says", "Zimmerman", "King", "Baptist",
			"Driver", "Tiberias", "Band", "Smith", "Zealot", "Iscariot" };
	String emailList[] = new String[] { "simon@says.com",
			"andre@zimmerman.com", "james@king.com", "john@baptist.com",
			"philip@driver.com", "bartholomew@tiberias.com",
			"matthew@band.com", "thaddaeus@smith.com", "simon@zealot.com",
			"judas@iscariot.com" };
	String phoneList[] = new String[] { "123-456-7890", "456-789-0123",
			"789-012-3456", "234-567-8901", "123-456-7890", "456-789-0123",
			"789-012-3456", "234-567-8901", "789-012-3456", "234-567-8901" };
	int ageList[] = new int[] { 18, 21, 24, 19, 23, 31, 27, 16, 25, 32 };
	List<Disciple> list = null;
	List<HashMap<String, String>> onlineList;
	int onlineCount; 
	
	
	static final String TAG = "MAINACTIVITY";
	static final String[] _from = new String[] { "first", "last", "phone",
			"age", "email", "timestamp", "id"};
	static final int[] _to = new int[] { R.id.firstHolder, R.id.lastHolder,
			R.id.phoneHolder, R.id.ageHolder, R.id.emailHolder, R.id.timeHolder, R.id.idHolder};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// SETUP OUR CONTENT VIEW
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listview);
		registerForContextMenu(listView);

		// INITIALIZE OUR LOCAL SQL DB AND ONLINE DB HANDLERS
		db = new DatabaseHandler(this);
		onlineDB = new OnlineDataHandler();
		
		// SETUP PARSE
		Parse.initialize(this, "wV4fx6jyR8xSFl0UX0k2jXAelPk2cbh0yToz87oa", "VpsMRjeYjurtnxWJ6njpRV5s38FOVxEofeBFgYvV");
		ParseAnalytics.trackAppOpened(getIntent());
		
		try {
			syncData();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} // END OF ONCREATE

	// REFRESH OUR TABLE ON START OF ACTIVITY
	public void onStart() {
		super.onStart();
		// CALL THE REFRESH TABLE FUNCTION
		reloadTable(null);
	}
	
	// SYNC OUR DATA WHEN WE LEAVE THIS ACTIVITY
	public void onStop() {
		super.onStop();
		try {
			syncData();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// REFRESH OUR TABLE
	public void reloadTable(ArrayList<HashMap<String, String>> myArrayList) {
		
		// IF WE'RE BEING PASSED EXISTING DATA, USE IT
		if (myArrayList != null) {
			discipleArrayList = myArrayList;
			
		// ELSE, PULL DATA FROM LOCAL SQL DB
		} else {
			discipleArrayList = queryDB();
		}
		
		// UPDATE OUR TABLE
		SimpleAdapter adapter = new SimpleAdapter(this, discipleArrayList,
				R.layout.disciplelist_row, _from, _to);
		listView.setAdapter(adapter);
		listView.invalidateViews();
	}
	

	@SuppressWarnings("static-access")
	public void syncData () throws ParseException {
		// IF WE HAVE DATA ONLINE BUT NO LOCAL DB, PULL THAT DATA DOWN
		/*if (getOnlineCount() > 0 && db.getDiscipleCount() == 0) {
			ArrayList<HashMap<String, String>> queryResults = new ArrayList<HashMap<String,String>>();
			queryResults = onlineDB.getAllOnline();
			reloadTable(queryResults);
		}
		// ELSE, IF WE HAVE LOCAL DATA BUT ONLINE IS EMPTY,
		// UPLOAD OUR LOCAL DATA 
		else*/ if (db.getDiscipleCount() > 0 && getOnlineCount() == 0){
			// GET ALL OUR LOCAL DATA
			List<Disciple> listOfAll = db.getAllDisciples();
			
			// CONVERT THEM TO PARSE OBJECTS AND ADD IT TO A LIST
			List<ParseObject> parseList = new ArrayList<ParseObject>();
			if (listOfAll != null) {
				for (Disciple item : listOfAll) {
					ParseObject pObject = new ParseObject("Disciple");
					pObject.put("first", item.getFirst());
					pObject.put("last", item.getLast());
					pObject.put("email", item.getEmail());
					pObject.put("phone", item.getPhone());
					pObject.put("age", item.getAge());
					pObject.put("localId", item.getID());
					parseList.add(pObject);
				}
			}
			
			// UPLOAD THE LIST TO OUR ONLINE DB
			ParseObject uploadObject = new ParseObject("Disciple");
			uploadObject.saveAllInBackground(parseList);
		}
		// ELSE, IF WE HAVE NO DATA LOCALLLY AND ONLINE, INSTALL STARTER DATA
		else if (db.getDiscipleCount() == 0 && getOnlineCount() == 0) {
			setupData(true); // TRUE FOR BOTH LOCAL AND ONLINE DATA
		}	
	}
	
	// SETUP INITIAL DEFAULT DATA
	private void setupData(boolean setupOnline) {
		
		// SETUP OUR LOCAL DATA
		for (int i = 0; i < firstNames.length; i++) {
			// USE LOCAL ARRAYS TO ADD DATA TO LOCAL SQL DB
			addDisciplesToDB(firstNames[i], lastNames[i], emailList[i],
					phoneList[i], ageList[i]);
		}

		// IF TRUE, ALSO SETUP THE ONLINE DATABASE
		if (setupOnline) {
			for (int i = 0; i < firstNames.length; i++) {
				// USE LOCAL ARRAYS TO ADD DATA TO ONLINE DB
				ParseObject newDisciple = new ParseObject("Disciple");
				newDisciple.put("localId", i+1);
				newDisciple.put("first", firstNames[i]);
				newDisciple.put("last", lastNames[i]);
				newDisciple.put("email", emailList[i]);
				newDisciple.put("phone", phoneList[i]);
				newDisciple.put("age", ageList[i]);
				newDisciple.saveInBackground();
			}
		}
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

	// ADD OUR DISCIPLES TO LOCAL SQL DATABASE
	public void addDisciplesToDB(String first, String last, String email,
		String phone, int age) {
		Disciple myDisciple = new Disciple();
		myDisciple.setFirst(first);
		myDisciple.setLast(last);
		myDisciple.setEmail(email);
		myDisciple.setPhone(phone);
		myDisciple.setAge(age);

		// ALSO ADD THIS SETUP DATA TO OUR LOCAL SQL DB
		db.addDisciple(myDisciple);
	}

	// QUERY OUR DISCIPLES FROM LOCAL SQL DATABASE
	public ArrayList<HashMap<String, String>> queryDB() {
		
		// EMPTY OUT OUR HOLDERS
		discipleArrayList = null;
		ArrayList<HashMap<String, String>> queryResults = null;

		// GET ALL DISCIPLES FROM LOCAL SQL DB AND RETURN IT
		list = db.getAllDisciples();
		if (list != null) {
			queryResults = new ArrayList<HashMap<String, String>>();
			for (Disciple item : list) {
				queryResults.add(createMap(item.getID(), item.getFirst(),
						item.getLast(), (Integer.toString(item.getAge())),
						item.getEmail(), item.getPhone(), item.getTimestamp()));
			}
		} else {
			Toast.makeText(MainActivity.this, "No disciples found.",
					Toast.LENGTH_LONG).show();
		}
		return queryResults;
	}

	// EDIT / DELETE CONTEXT MENU
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Choose Action");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {

		// EDIT DISCIPLE CHOICE BASED ON ID FOUND BY ROW
		case R.id.edit:
			// INTENT TO START VIEW ACTIVITY
			Intent intent = new Intent(MainActivity.this, EditActivity.class);
			intent.putExtra("EXTRA_SESSION_ID", Integer.parseInt(discipleArrayList.get((int) info.id).get("id")));
			MainActivity.this.startActivity(intent);

			// editNote(info.id);
			return true;

		// DELETE DISCIPLE CHOICE
		case R.id.delete:
			// SHOW A CONFIRMATION BUTTON
			new AlertDialog.Builder(this)
					.setMessage(
							"Are you sure you want to delete this disciple?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// PASS IN THE SELECTED ID TO BE DELETED
									deleteSelected(list.get((int) info.id));
								}
							}).setNegativeButton("No", null).show();
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	// DELETE THE SELECTED DISCIPLE
	private void deleteSelected(Disciple disciple) {

		// DELETE FUNCTION
		if (db.deleteDisciple(disciple)) {
			
			// DELETE FROM ONLINE DB
			onlineDB.deleteDisciple(disciple);
			
			// REFRESH TABLE LISTVIEW
			reloadTable(null);

			Toast.makeText(MainActivity.this,
					disciple.getFirst() + " was successfully deleted.",
					Toast.LENGTH_LONG).show();

		// DELETE FAILED
		} else {
			Toast.makeText(MainActivity.this, "Delete failed.",
					Toast.LENGTH_LONG).show();
		}

	}

	// ADDING ACTION BAR MENU WITH ADD OPTION
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_new:
			onAddActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// IF USER CLICKS ADD ICON FROM MAIN ACTION BAR
	public void onAddActivity() {
		// INTENT TO START ADD ACTIVITY
		Intent intent = new Intent(MainActivity.this, AddActivity.class);
		MainActivity.this.startActivity(intent);
	}

	// FILTER OUR LIST VIEW
	public void sortTable(View view) {
		switch (view.getId()) {
		case R.id.firstFilter:
			reloadSortTable("first");
			break;	
		case R.id.lastFilter:
			reloadSortTable("last");
			break;	
		case R.id.ageFilter:
			reloadSortTable("age");
			break;	
		}

	}

	// RELOAD THE TABLE WITH OUR FILTER
	public void reloadSortTable(String filter) {

		// EMPTY OUT OUR HOLDERS
		discipleArrayList = null;
		ArrayList<HashMap<String, String>> queryResults = null;

		List<Disciple> list = db.sortTable(filter);

		if (list != null) {
			queryResults = new ArrayList<HashMap<String, String>>();
			for (Disciple item : list) {
				queryResults.add(createMap(item.getID(), item.getFirst(),
						item.getLast(), (Integer.toString(item.getAge())),
						item.getEmail(), item.getPhone(), item.getTimestamp()));
			}
		} else {
			Toast.makeText(MainActivity.this, "No disciples found.",
					Toast.LENGTH_LONG).show();
		}
		reloadTable(queryResults);

	}

	public int getOnlineCount() throws ParseException {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Disciple");

		// CHECK IF THERE'S DATA IN PARSE
		query = ParseQuery.getQuery("Disciple");
			onlineCount = query.count();
			Log.i(TAG, Integer.toString(onlineCount));
		
		return onlineCount;
	}


}
