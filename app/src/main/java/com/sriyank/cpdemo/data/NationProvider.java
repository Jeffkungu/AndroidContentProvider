package com.sriyank.cpdemo.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.sriyank.cpdemo.data.NationContract.CONTENT_AUTHORITY;
import static com.sriyank.cpdemo.data.NationContract.PATH_COUNTRIES;

public class NationProvider extends ContentProvider {

	private static final String TAG = NationProvider.class.getSimpleName();

	private NationDbHelper databaseHelper;

	//constants for the operation
	private static final int COUNTRIES = 1;				// For whole table
	private static final int COUNTRIES_ID = 2;			// For a specific row in a table identified by _ID
	private static final int COUNTRIES_COUNTRY_NAME = 3;// For a specific row in a table identified by COUNTRY NAME

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES, COUNTRIES);
		uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES + "/#", COUNTRIES_ID);
		uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES + "/*", COUNTRIES_COUNTRY_NAME);
	}

	@Override
	public boolean onCreate() {
		databaseHelper = new NationDbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		Cursor cursor = null;

		switch (uriMatcher.match(uri)) {

			default:
				throw new IllegalArgumentException(TAG + "Unknown URI: " + uri);
		}

//		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		switch (uriMatcher.match(uri)) {
			case(COUNTRIES):
				return insertRecord(uri, values, NationContract.NationEntry.TABLE_NAME);

			default:
				throw new IllegalArgumentException(TAG + "Unknown URI: " + uri);
		}
	}

	private Uri insertRecord(Uri uri, ContentValues values, String tableName) {

		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		long rowId = database.insert(tableName, null, values);

		if (rowId == -1) {
			Log.e(TAG, "Insert error for Uri" + uri);
			return null;
		}
		return ContentUris.withAppendedId(uri, rowId);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		switch (uriMatcher.match(uri)) {

			default:
				throw new IllegalArgumentException(TAG + "Unknown URI: " + uri);
		}
	}

	private int deleteRecord(String selection, String[] selectionArgs, String tableName) {
		return -1;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		switch (uriMatcher.match(uri)) {

			default:
				throw new IllegalArgumentException(TAG + "Unknown URI: " + uri);
		}
	}

	private int updateRecord(ContentValues values, String selection, String[] selectionArgs, String tableName) {
		return -1;
	}
}
