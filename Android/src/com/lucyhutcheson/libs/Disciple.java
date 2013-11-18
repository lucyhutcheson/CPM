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

public class Disciple {

	// VARIABLES
	int _id;
	String _first;
	String _last;
	String _email;
	String _phone;
	int _age;
	int _timestamp;

	// EMPTY CONSTRUCTOR
	public Disciple() {

	}

	// CONSTRUCTOR
	public Disciple(int id, String first, String last, String email, String phone, int age, int timestamp) {
		this._id = id;
		this._first = first;
		this._last = last;
		this._email = email;
		this._phone = phone;
		this._age = age;
		this._timestamp = timestamp;
	}
	
	// CONSTRUCTOR
	public Disciple(String first, String last, String email, String phone, int age) {
		this._first = first;
		this._last = last;
		this._email = email;
		this._phone = phone;
		this._age = age;
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
	public int getTimestamp() {
		return this._timestamp;
	}
	// SET AGE
	public void setTimestamp(int timestamp) {
		this._timestamp = timestamp;
	}
	
}

