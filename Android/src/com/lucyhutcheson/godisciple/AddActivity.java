/*
 * project		GoDisciple
 * 
 * package		com.lucyhutcheson.godisciple
 * 
 * @author		Lucy Hutcheson
 * 
 * date			Nov 17, 2013
 * 
 */
package com.lucyhutcheson.godisciple;


import com.lucyhutcheson.libs.DatabaseHandler;
import com.lucyhutcheson.libs.Disciple;
import com.lucyhutcheson.libs.OnlineDataHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity {

	// SETUP VARIABLES FOR CLASS
	public static final String TAG = "ADDACTIVITY";
	Context context = this;
	DatabaseHandler db;
	OnlineDataHandler onlineDB;
	MainActivity main;
	static EditText _firstField;
	static EditText _lastField;
	static EditText _emailField;
	static EditText _phoneField;
	static EditText _ageField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// SETUP ACTIVITY
		setContentView(R.layout.activity_add);
		_firstField = (EditText) findViewById(R.id.firstTextField);
		_lastField = (EditText) findViewById(R.id.lastTextField);
		_emailField = (EditText) findViewById(R.id.emailTextField);
		_phoneField = (EditText) findViewById(R.id.phoneTextField);
		_ageField = (EditText) findViewById(R.id.ageTextField);

		// INITIAZLIZE THE LOCAL AND ONLINE DBS
		db = new DatabaseHandler(this);
		onlineDB = new OnlineDataHandler();

	}

	public void onSubmit() {

		// CHECK IF OUR TITLE, DATE, OR TIME FIELDS ARE EMPTY
		// NOTIFY USER THAT THERE ARE EMPTY FIELDS

		if (_firstField.getText().toString().equals("")) {
			Toast.makeText(this, "Please enter a first name.", Toast.LENGTH_SHORT).show();
		} else if (_lastField.getText().toString().equals("")) {
			Toast.makeText(this, "Please enter a last name.", Toast.LENGTH_SHORT).show();
		} else if (_emailField.getText().toString().equals("")) {
			Toast.makeText(this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
		} else if (_phoneField.getText().toString().equals("")) {
			Toast.makeText(this, "Please enter a phone number.", Toast.LENGTH_SHORT).show();
		} else if (_ageField.getText().toString().equals("")) {
			Toast.makeText(this, "Please enter an age.", Toast.LENGTH_SHORT).show();

		// ALL NECESSARY DATA HAS BEEN ENTERED, PROCEED WITH SAVING
		} else {

			String first = _firstField.getText().toString();
			String last = _lastField.getText().toString();
			String email = _emailField.getText().toString();
			String phone = _phoneField.getText().toString();
			int age = Integer.parseInt(_ageField.getText().toString());
			
			// SETUP LOCAL DISCIPLE OBJECT
			Disciple myDisciple = new Disciple();
			myDisciple.setFirst(first);
			myDisciple.setLast(last);
			myDisciple.setEmail(email);
			myDisciple.setPhone(phone);
			myDisciple.setAge(age);

			// ADD DATA TO OUR LOCAL SQL DB
			db.addDisciple(myDisciple);
			
			/*// SETUP ONLINE DISCIPLE OBJECT
			int newLocalID = db.getDiscipleCount(); // GET OUR CURRENT LIST AND ADD 1
			OnlineDisciple onlineDisciple = new OnlineDisciple();
			onlineDisciple.setID(newLocalID);
			onlineDisciple.setFirst(first);
			onlineDisciple.setLast(last);
			onlineDisciple.setEmail(email);
			onlineDisciple.setPhone(phone);
			onlineDisciple.setAge(age);
			
			// ADD DATA TO OUR ONLINE DB
			onlineDB.addDisciple(onlineDisciple);*/

			
			// Notify the user what they just did
			Toast.makeText(
					this,
					"Woohoo! Disciple successfully saved! ", Toast.LENGTH_SHORT)
					.show();

			// FINISH
			((AddActivity) this).finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			AddActivity.this.finish();
			return true;
		case R.id.action_save:
			onSubmit();
			return true;
		case R.id.action_cancel:
			AddActivity.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

}
