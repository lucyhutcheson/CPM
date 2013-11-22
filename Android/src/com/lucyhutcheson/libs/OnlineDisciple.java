/*
 * project		GoDisciple
 * 
 * package		com.lucyhutcheson.libs
 * 
 * @author		Lucy Hutcheson
 * 
 * date			Nov 20, 2013
 * 
 */
package com.lucyhutcheson.libs;


public class OnlineDisciple  {
	
	int _id;
	String _first;
	String _last;
	String _email;
	String _phone;
	int _age;
	long _timestamp;

	
	// EMPTY CONSTRUCTOR
	public OnlineDisciple() {
		
	}
	
	// CONSTRUCTOR
	public OnlineDisciple(int id, String first, String last, String email, String phone, int age, long timestamp) {
		this._id = id;
		this._first = first;
		this._last = last;
		this._email = email;
		this._phone = phone;
		this._age = age;
		this._timestamp = timestamp;
	}
	
	// CONSTRUCTOR
	public OnlineDisciple(String first, String last, String email, String phone, int age) {
		this._first = first;
		this._last = last;
		this._email = email;
		this._phone = phone;
		this._age = age;
	}

	// CONSTRUCTOR
	public OnlineDisciple(String first, String last, String email, String phone, int age, int id) {
		this._first = first;
		this._last = last;
		this._email = email;
		this._phone = phone;
		this._age = age;
		this._id = id;
	}


	// GET ID
	public int getID() {
		return this._id;
	}
	// SET ID
	public void setID(int id) {
		this._id = id;
	}

	// GET FIRST
	public String getFirst() {
		return this._first;
	}
	// SET FIRST
	public void setFirst(String first) {
		this._first = first;
	}

	// GET LAST
	public String getLast() {
		return this._last;
	}
	// SET LAST
	public void setLast(String last) {
		this._last = last;
	}

	// GET EMAIL
	public String getEmail() {
		return this._email;
	}
	// SET EMAIL
	public void setEmail(String email) {
		this._email = email;
	}
	
	// GET PHONE
	public String getPhone() {
		return this._phone;
	}
	// SET PHONE
	public void setPhone(String phone) {
		this._phone = phone;
	}

	// GET AGE
	public int getAge() {
		return this._age;
	}
	// SET AGE
	public void setAge(int age) {
		this._age = age;
	}

	// GET TIMESTAMP
	public long getTimestamp() {
		return this._timestamp;
	}
	// SET AGE
	public void setTimestamp(long timestamp) {
		this._timestamp = timestamp;
	}
}
