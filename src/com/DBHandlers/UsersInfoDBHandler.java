package com.DBHandlers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersInfoDBHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "HotelManagerUsers	";
	// Contacts table name
	private static final String TABLE_USERS_CONFIG = "UsersInfo";

	// Contacts Table Columns names
	private static final String KEY_UN = "UN";
	private static final String KEY_PWD = "PWD";

	public UsersInfoDBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
	// Adding new contact
	public void addNewUserDetails(UsersInfo dc) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_UN, dc.getUN()); // Image button id
		values.put(KEY_PWD, dc.getPWD()); // Device Id
		// Inserting Row
		db.insert(TABLE_USERS_CONFIG, null, values);
		db.close(); // Closing database connection
	}

	public void deleteAllUsersDetails() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS_CONFIG, "", null);

	}

	// Deleting single contact
	public void deleteUserDetails(UsersInfo contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS_CONFIG, KEY_UN + " = ?", new String[] { String
				.valueOf(contact.getUN()) });
		db.close();

	}

	// Getting All Contacts
	public List<UsersInfo> getAllUsersDetails() {
		List<UsersInfo> usersList = new ArrayList<UsersInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS_CONFIG;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				UsersInfo contact = new UsersInfo();
				contact.setUN(cursor.getString(0));
				contact.setPWD(cursor.getString(1));

				// Adding user to list
				usersList.add(contact);
			} while (cursor.moveToNext());
		}

		// return user list
		return usersList;
	}

	// Getting All Contacts
	public UsersInfo getUsersInfoByUN(String UN) {
		UsersInfo contactList = new UsersInfo();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS_CONFIG + " WHERE "
				+ KEY_UN + "='" + UN + "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			contactList.setUN(cursor.getString(0));
			contactList.setPWD(cursor.getString(1));
		}
		// return contact list
		return contactList;
	}

	// Getting Users Count
	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USERS_CONFIG;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
		// return 2;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USERS_CONFIG
				+ "(" + KEY_UN + " TEXT PRIMARY KEY," + KEY_PWD + " TEXT)";
		db.execSQL(CREATE_CONTACTS_TABLE);
		UsersInfo uinfo = new UsersInfo("admin", "admin");
		insertUserInfo(uinfo);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS_CONFIG);

		// Create tables again
		onCreate(db);
	}

	// Updating single user info
	public int updateUserInfo(UsersInfo uinfo) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_UN, uinfo.getUN());
		values.put(KEY_PWD, uinfo.getPWD());
		// updating row
		return db.update(TABLE_USERS_CONFIG, values, KEY_UN + " = ?",
				new String[] { String.valueOf(uinfo.getUN()) });
	}

	// Updating single contact
	public long insertUserInfo(UsersInfo uinfo) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_UN, uinfo.getUN());
		values.put(KEY_PWD, uinfo.getPWD());
		return db.insert(TABLE_USERS_CONFIG, null, values);
	}

}
