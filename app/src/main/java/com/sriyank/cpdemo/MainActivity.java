package com.sriyank.cpdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sriyank.cpdemo.data.NationContract.NationEntry;
import com.sriyank.cpdemo.data.NationDbHelper;

/**
 * 	Module4: Implementing ContentProvider with It’s Insert and Query Operations
 *	Author: Sriyank Siddhartha
 *
 *		"BEFORE" project
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private EditText etCountry, etContinent, etWhereToUpdate, etNewContinent, etWhereToDelete, etQueryRowById;
	private Button btnInsert, btnUpdate, btnDelete, btnQueryRowById, btnDisplayAll;

	private static final String TAG = MainActivity.class.getSimpleName();

	private SQLiteDatabase database;
	private NationDbHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etCountry 		= (EditText) findViewById(R.id.etCountry);
		etContinent 	= (EditText) findViewById(R.id.etContinent);
		etWhereToUpdate = (EditText) findViewById(R.id.etWhereToUpdate);
		etNewContinent 	= (EditText) findViewById(R.id.etUpdateContinent);
		etQueryRowById 	= (EditText) findViewById(R.id.etQueryByRowId);
		etWhereToDelete = (EditText) findViewById(R.id.etWhereToDelete);

		btnInsert 		= (Button) findViewById(R.id.btnInsert);
		btnUpdate 		= (Button) findViewById(R.id.btnUpdate);
		btnDelete 		= (Button) findViewById(R.id.btnDelete);
		btnQueryRowById = (Button) findViewById(R.id.btnQueryByID);
		btnDisplayAll 	= (Button) findViewById(R.id.btnDisplayAll);

		btnInsert.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnQueryRowById.setOnClickListener(this);
		btnDisplayAll.setOnClickListener(this);

		databaseHelper = new NationDbHelper(this);
		database = databaseHelper.getWritableDatabase();		// READ/WRITE
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.btnInsert:
				insert();
				break;

			case R.id.btnUpdate:
				update();
				break;

			case R.id.btnDelete:
				delete();
				break;

			case R.id.btnQueryByID:
				queryRowById();
				break;

			case R.id.btnDisplayAll:
				queryAndDisplayAll();
				break;
		}
	}

	private void insert() {

		String countryName 	= etCountry.getText().toString();
		String continentName= etContinent.getText().toString();

		if(countryName != null && continentName != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(NationEntry.COLUMN_COUNTRY, countryName);
			contentValues.put(NationEntry.COLUMN_CONTINENT, continentName);

			Uri uri = NationEntry.CONTENT_URI;
			Uri uriRowInserted = getContentResolver().insert(uri, contentValues);
			Log.i(TAG, "Items inserted at" + uriRowInserted);
		}
		Toast.makeText(this, "Country and Continent can not be null", Toast.LENGTH_SHORT).show();
	}

	private void update() {

		String whereCountry = etWhereToUpdate.getText().toString();
		String newContinent = etNewContinent.getText().toString();

		if(whereCountry != null && newContinent != null) {
			String[] projection = {
					NationEntry._ID,
					NationEntry.COLUMN_COUNTRY,
					NationEntry.COLUMN_CONTINENT
			};
			String selection = NationEntry.COLUMN_COUNTRY + " = ?";
			String[] selectionArgs = { whereCountry };			// WHERE country = ? = Japan

			String sortOrder = null;
			Cursor cursor = database.query(NationEntry.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			if(cursor != null && cursor.moveToNext()) {
				ContentValues contentValues = new ContentValues();
				contentValues.put(NationEntry.COLUMN_CONTINENT, newContinent);

				int rowsUpdated = database.update(NationEntry.TABLE_NAME, contentValues, selection, selectionArgs);
				Log.i(TAG, "Number of rows updated: " + rowsUpdated);
			}
			Toast.makeText(this, whereCountry.toString() + " is not in the data base",
					Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(this, "Country and Continent can not be null", Toast.LENGTH_SHORT).show();
	}

	private void delete() {

		String countryName = etWhereToDelete.getText().toString();

		String selection = NationEntry.COLUMN_COUNTRY + " = ? ";
		String[] selectionArgs = { countryName };		// WHERE country = "Japan"

		int rowsDeleted = database.delete(NationEntry.TABLE_NAME, selection, selectionArgs);
		Log.i(TAG, "Number of rows deleted: " + rowsDeleted);
	}

	private void queryRowById() {

		String rowId = etQueryRowById.getText().toString();

		String[] projection = {
				NationEntry._ID,
				NationEntry.COLUMN_COUNTRY,
				NationEntry.COLUMN_CONTINENT
		};

		// Filter results. Make these null if you want to query all rows
		String selection = NationEntry._ID + " = ? ";	// _id = ?
		String[] selectionArgs = { rowId };				// Replace '?' by rowId in runtime

		String sortOrder = null;	// Ascending or Descending ...

		Cursor cursor = database.query(NationEntry.TABLE_NAME, projection, selection,
										selectionArgs, null, null, sortOrder);

		if (cursor != null && cursor.moveToNext()) {

			String str = "";

			String[] columns = cursor.getColumnNames();
			for (String column : columns) {
				str += "\t" + cursor.getString(cursor.getColumnIndex(column));
			}
			str += "\n";

			cursor.close();
			Log.i(TAG, str);
		}
	}

	private void queryAndDisplayAll() {

		String[] projection = {
				NationEntry._ID,
				NationEntry.COLUMN_COUNTRY,
				NationEntry.COLUMN_CONTINENT
		};

		// Filter results. Make these null if you want to query all rows
		String selection = null;
		String[] selectionArgs = null;

		String sortOrder = null;	// Ascending or Descending ...

		Cursor cursor = database.query(NationEntry.TABLE_NAME, projection, selection,
										selectionArgs, null, null, sortOrder);

		if (cursor != null) {

			String str = "";
			while (cursor.moveToNext()) {	// Cursor iterates through all rows

				String[] columns = cursor.getColumnNames();
				for (String column : columns) {
					str += "\t" + cursor.getString(cursor.getColumnIndex(column));
				}
				str += "\n";
			}

			cursor.close();
			Log.i(TAG, str);
		}
	}

	@Override
	protected void onDestroy() {
		database.close();	// Close Database Connection when activity is destroyed
		super.onDestroy();
	}
}
