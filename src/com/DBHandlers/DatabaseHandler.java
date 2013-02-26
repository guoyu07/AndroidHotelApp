package com.DBHandlers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "HotelManager";
	// Contacts table name
	private static final String TABLE_DEVICE_CONFIG = "DeviceConfig";

	// Contacts Table Columns names
	private static final String KEY_IMGBTNID = "ImgBtnId";
	private static final String KEY_DEVICEID = "DeviceId";
	private static final String KEY_HEXACODE = "HexaCode";
	private static final String KEY_ISCOFIGED = "IsConfigured";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addDeviceConfig(DeviceConfig dc) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_IMGBTNID, dc.getImgBtnId()); // Image button id
		values.put(KEY_DEVICEID, dc.getDeviceId()); // Device Id
		values.put(KEY_HEXACODE, dc.getHexaCode());
		values.put(KEY_ISCOFIGED, dc.getIsConfigured());
		// Inserting Row
		db.insert(TABLE_DEVICE_CONFIG, null, values);
		db.close(); // Closing database connection
	}

	public void DeleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DEVICE_CONFIG, "", null);

	}

	// Deleting single contact
	public void deleteDeviceConfig(DeviceConfig contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DEVICE_CONFIG, KEY_IMGBTNID + " = ?",
				new String[] { String.valueOf(contact.getImgBtnId()) });
		db.close();

	}

	// Getting All Contacts
	public List<DeviceConfig> getAllDeviceConfigs() {
		List<DeviceConfig> contactList = new ArrayList<DeviceConfig>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DEVICE_CONFIG;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				DeviceConfig contact = new DeviceConfig();
				contact.setImgBtnId(cursor.getString(0));
				contact.setDeviceId(cursor.getString(1));
				contact.setHexaCode(cursor.getString(2));
				contact.setIsConfigured(Integer.parseInt(cursor.getString(3)));

				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Getting All Contacts
	public DeviceConfig getDeviceConfigsByCtrlId(int Id) {
		DeviceConfig contactList = new DeviceConfig();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DEVICE_CONFIG
				+ " WHERE " + KEY_IMGBTNID + "='" + Id + "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			/* do { */
			DeviceConfig contact = new DeviceConfig();
			contact.setImgBtnId(cursor.getString(0));
			contact.setDeviceId(cursor.getString(1));
			contact.setHexaCode(cursor.getString(2));
			contact.setIsConfigured(Integer.parseInt(cursor.getString(3)));

			/* } while (cursor.moveToNext()); */
		}

		// return contact list
		return contactList;
	}

	/** Getting configured contacts Count */
	public int getConfigeredDevicesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_DEVICE_CONFIG + " WHERE "
				+ KEY_ISCOFIGED + "=1";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
		// return 2;
	}

	// Getting single contact
	DeviceConfig getDeviceConfig(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_DEVICE_CONFIG, new String[] {
				KEY_IMGBTNID, KEY_DEVICEID, KEY_HEXACODE, KEY_ISCOFIGED },
				KEY_IMGBTNID + "=?", new String[] { String.valueOf(id) }, null,
				null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		DeviceConfig contact = new DeviceConfig(cursor.getString(0), cursor
				.getString(1), cursor.getString(2), cursor.getInt(3));
		// return contact
		return contact;
	}

	// Getting contacts Count
	public int getDeviceConfigCount() {
		String countQuery = "SELECT  * FROM " + TABLE_DEVICE_CONFIG;
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
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DEVICE_CONFIG
				+ "(" + KEY_IMGBTNID + " TEXT PRIMARY KEY," + KEY_DEVICEID
				+ " TEXT" + "," + KEY_HEXACODE + " TEXT" + "," + KEY_ISCOFIGED
				+ " INTEGER)";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE_CONFIG);

		// Create tables again
		onCreate(db);

	}

	// Updating single contact
	public int updateDeviceConfig(DeviceConfig contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IMGBTNID, contact.getImgBtnId());
		values.put(KEY_DEVICEID, contact.getDeviceId());
		values.put(KEY_HEXACODE, contact.getHexaCode());
		values.put(KEY_ISCOFIGED, contact.getIsConfigured());

		// updating row
		return db.update(TABLE_DEVICE_CONFIG, values, KEY_IMGBTNID + " = ?",
				new String[] { String.valueOf(contact.getImgBtnId()) });
	}

	// Updating single contact
	public int updateDeviceConfigByImgId(DeviceConfig contact, int ImgId) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IMGBTNID, contact.getImgBtnId());
		values.put(KEY_DEVICEID, contact.getDeviceId());
		values.put(KEY_HEXACODE, contact.getHexaCode());
		values.put(KEY_ISCOFIGED, contact.getIsConfigured());

		// updating row
		return db.update(TABLE_DEVICE_CONFIG, values, KEY_IMGBTNID + " = '"
				+ ImgId + "'", new String[] { String.valueOf(contact
				.getImgBtnId()) });
	}

	// Updating single contact
	public long insertDeviceConfig(DeviceConfig contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IMGBTNID, contact.getImgBtnId());
		values.put(KEY_DEVICEID, contact.getDeviceId());
		values.put(KEY_HEXACODE, contact.getHexaCode());
		values.put(KEY_ISCOFIGED, contact.getIsConfigured());
		return db.insert(TABLE_DEVICE_CONFIG, null, values);
		// updating row
		/*
		 * return db.update(TABLE_DEVICE_CONFIG, values, KEY_IMGBTNID + " = '" +
		 * ImgId + "'", new String[] { String.valueOf(contact .getImgBtnId())
		 * });
		 */
	}

}
